const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const DATABASE_ID = "ai-studio-android-mrbook-09b9f372-8cb8-476d-8ad9-3a4695d97793";
const ALICE_UID = "alice_reader_1";
const BOB_UID = "bob_reader_2";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function validProfile(uid) {
  const now = new Date(Date.now() - 1000);
  return {
    userId: uid,
    characterName: "Aria",
    avatarId: "scholar",
    cloakColorIndex: 0,
    statusText: "Exploring Science Wing",
    totalPagesRead: 4,
    totalStudyMinutes: 10,
    booksCompleted: 0,
    lampAmbienceId: "golden_study",
    lampBrightness: 0.85,
    createdAt: now,
    updatedAt: now,
  };
}

function validProgress(uid, bookId) {
  const now = new Date(Date.now() - 1000);
  return {
    userId: uid,
    bookId,
    bookTitle: "Why the Sky Glows Blue",
    categoryId: "SCIENCE",
    currentPage: 1,
    totalPages: 4,
    isBookmarked: false,
    bookmarkPage: 0,
    isCompleted: false,
    status: "reading",
    createdAt: now,
    updatedAt: now,
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user cannot read or write profiles", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore(DATABASE_ID);
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).set(validProfile(ALICE_UID)));
});

test("Authenticated owner can create and update own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore(DATABASE_ID);
  const profileRef = aliceDb.collection("users").doc(ALICE_UID);
  await assertSucceeds(profileRef.set(validProfile(ALICE_UID)));
  await assertSucceeds(
    profileRef.update({
      statusText: "Reading at Table A",
      totalPagesRead: 5,
      updatedAt: new Date(Date.now() - 500),
    })
  );
});

test("Rejects shadow ghost field on profile creation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore(DATABASE_ID);
  const bad = { ...validProfile(ALICE_UID), isAdmin: true };
  await assertFails(aliceDb.collection("users").doc(ALICE_UID).set(bad));
});

test("Cross-user isolation blocks Bob from reading or writing Alice progress and history", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore(DATABASE_ID);
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("book_progress").doc("sci_school").set(validProgress(ALICE_UID, "sci_school"))
  );

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore(DATABASE_ID);
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("book_progress").doc("sci_school").get()
  );
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).collection("reading_history").get()
  );
});

test("Authenticated owner can write reading history and margin notes", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore(DATABASE_ID);
  const now = new Date(Date.now() - 1000);
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("reading_history").doc("hist_1").set({
      userId: ALICE_UID,
      bookId: "sci_school",
      bookTitle: "Why the Sky Glows Blue",
      categoryName: "Science",
      levelName: "School",
      action: "opened",
      pageNumber: 1,
      createdAt: now,
    })
  );
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("margin_notes").doc("note_1").set({
      userId: ALICE_UID,
      bookId: "sci_school",
      bookTitle: "Why the Sky Glows Blue",
      pageNumber: 1,
      noteText: "Blue light scatters fastest!",
      createdAt: now,
    })
  );
});
