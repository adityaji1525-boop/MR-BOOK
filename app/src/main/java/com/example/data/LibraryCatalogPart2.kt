package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.BookPage
import com.example.model.EducationLevel

internal object LibraryCatalogPart2 {
    val books: List<Book> = listOf(
        // ================= LITERATURE (Shelves 5–6) =================
        Book(
            id = "lit_school",
            title = "The Lantern Tree Letters",
            author = "Clara Meadow",
            category = BookCategory.LITERATURE,
            level = EducationLevel.SCHOOL,
            shelfNumber = 5,
            spineColor = Color(0xFF8C2D46),
            coverTrimColor = Color(0xFFE7C178),
            realSubjectScope = "Elementary Literature, Epistolary Form, Figurative Language, Character Empathy, Plot Structure & Reading Comprehension",
            completeTopics = listOf(
                "Epistolary Literature: Letters, Diaries & Voice",
                "Story Elements: Setting, Character, Conflict & Resolution",
                "Figurative Language: Simile, Metaphor & Personification",
                "Sensory Imagery & Seasonal Symbolism",
                "Tone, Mood & Point of View (First-Person)",
                "Summarizing Central Theme & Supporting Details"
            ),
            synopsis = "Complete school literature companion exploring the epistolary novel form, figurative language, sensory imagery, character growth, and reading comprehension.",
            dedication = "For friends who keep each other's stories safe.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Epistolary Voice & Setting (Letter One)",
                    "Epistolary stories tell a complete narrative through letters, postcards, or journal entries. In Letter One—dated October—Maya writes beside an oak hollow: 'The frost-gold maple leaf sits between pages like a tiny lantern.' First-person narration lets readers hear exact thoughts, feelings, and sensory clues (sound of wind, crisp parchment) directly from the character's perspective.",
                    "Literary Term: Epistolary Fiction (from Latin 'epistola', meaning letter).",
                    "Why does reading someone's letter feel closer than watching from afar?"
                ),
                BookPage(
                    2,
                    "2. Figurative Language: Simile, Metaphor & Personification",
                    "Authors paint vivid pictures using figurative comparisons. A simile connects ideas with 'like' or 'as' ('the frost feathers crept like silver lace'); a metaphor equates them directly ('our book club is an anchor across miles'); personification gives living qualities to objects ('the winter wind tapped politely on the library glass').",
                    "Examples: Simile ('quiet as snowfall') | Metaphor ('words are bridges') | Personification ('the clock whispered').",
                    "Invent one simile and one metaphor describing your reading lamp."
                ),
                BookPage(
                    3,
                    "3. Narrative Arc: Conflict, Turning Point & Theme",
                    "Every satisfying chapter book moves through Exposition (friendship across streets), Rising Action (snowstorm delays mail), Climax (discovering hidden pressed paper stars in spring), Falling Action, and Resolution. Beneath plot events lies the Theme—the enduring truth about loyalty, patience, and shared curiosity.",
                    "Plot Diagram: Exposition -> Inciting Incident -> Rising Action -> Climax -> Resolution.",
                    "Summarize the central theme of a favorite childhood book in one sentence."
                ),
                BookPage(
                    4,
                    "4. Active Reading & Comprehension Strategies",
                    "Strong readers practice four habits: predicting outcomes from textual clues, visualizing scenes, asking 'why' about character choices, and summarizing main ideas in their own words. Use live Google Search below to explore classic epistolary novels for young readers like 'Daddy-Long-Legs' or 'Letters from Rifka'.",
                    "Reading Toolkit: Predict -> Question -> Clarify -> Summarize.",
                    "Write a three-sentence keepsake letter summarizing what you learned today."
                )
            )
        ),
        Book(
            id = "lit_high",
            title = "Sonnets & Selected Classics",
            author = "William Hargrave (Ed.)",
            category = BookCategory.LITERATURE,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 5,
            spineColor = Color(0xFF6B1D2F),
            coverTrimColor = Color(0xFFD9AE5F),
            realSubjectScope = "High School Literature & Poetry: Prosody, Sonnet Traditions, Literary Devices, Dramatic Tragedy, Close Reading & Literary Movements",
            completeTopics = listOf(
                "Prosody, Rhythm, Meter & Iambic Pentameter",
                "Petrarchan (Italian) vs Shakespearean Sonnet Structure",
                "Extended Metaphor, Conceit, Irony & Symbolism",
                "Freytag's Pyramid, Tragic Flaw (Hamartia) & Catharsis",
                "Close Reading, Diction, Syntax & Textual Evidence",
                "Renaissance, Romanticism, Victorian & Modernist Eras"
            ),
            synopsis = "Complete high-school poetry and classics handbook covering scansion, Shakespearean & Petrarchan sonnets, irony, tragic structure, and major literary eras.",
            dedication = "Poetry is ordinary speech lit from within by a lamp.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Prosody, Meter & Iambic Pentameter",
                    "English poetic meter arranges stressed (/) and unstressed (˘) syllables into rhythmic feet. An iamb follows da-DUM (˘ /); five iambs per line form Iambic Pentameter (10 syllables total)—found throughout Shakespeare, Milton, and Wordsworth. Poets intentionally break meter (trochaic inversions or caesura pauses) to mirror emotional shock or hesitation.",
                    "Scansion: Shall I / com-PARE / thee TO / a SUM- / mer's DAY? (Sonnet 18).",
                    "Read a famous line aloud and tap out its stressed beats."
                ),
                BookPage(
                    2,
                    "2. Sonnet Forms: Petrarchan & Shakespearean",
                    "A classic 14-line sonnet hinges on a volta (conceptual turn). The Petrarchan sonnet splits into an 8-line octave (ABBAABBA) posing a dilemma and a 6-line sestet (CDECDE) answering it. The Shakespearean sonnet groups three quatrains (ABAB CDCD EFEF) building an argument before a punchy rhymed couplet (GG) flips or seals the meaning.",
                    "Structure: 14 lines in Iambic Pentameter centered around the Volta (turn).",
                    "Notice how Sonnet 130 flips romantic clichés cleanly in its final couplet!"
                ),
                BookPage(
                    3,
                    "3. Irony, Symbolism & Dramatic Tragedy",
                    "Irony takes three forms: verbal (saying the opposite), situational (outcome defies expectation), and dramatic (the audience knows a fatal secret unseen by characters, as in Sophocles' Oedipus Rex or Macbeth). Classical tragedy examines hamartia (error in judgment), peripeteia (reversal of fortune), and catharsis (emotional clarity).",
                    "Aristotle's Poetics: Hamartia -> Peripeteia -> Anagnorisis (recognition) -> Catharsis.",
                    "Compare dramatic irony in Shakespeare's Romeo and Juliet with modern stories."
                ),
                BookPage(
                    4,
                    "4. Literary Movements & Close Reading Evidence",
                    "Literature evolved through distinct philosophical waves: Renaissance humanism (individual agency), Enlightenment satire (Swift, Pope), Romanticism (nature & sublime emotion—Keats, Shelley, Mary Shelley), Victorian social realism (Dickens, Brontë, George Eliot), and 20th-century Modernism (fragmented time—Woolf, Eliot). Analytical essays anchor every claim directly in quoted diction and syntax.",
                    "Analysis Frame: Claim -> Embedded Quotation -> Word-Level Diction & Syntax Analysis.",
                    "Use live Google Search to inspect complete texts or scholarly notes on any classic work."
                )
            )
        ),
        Book(
            id = "lit_uni",
            title = "World Epics & Narrative Voice",
            author = "Prof. S. Takahashi",
            category = BookCategory.LITERATURE,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 6,
            spineColor = Color(0xFF561526),
            coverTrimColor = Color(0xFFE9C573),
            realSubjectScope = "University Comparative Literature & Narratology: World Epics, Gérard Genette Focalization, Modernist Interiority, Postcolonial Canon & Translation Studies",
            completeTopics = listOf(
                "Oral Epics: Gilgamesh, Homer, Mahabharata & Formulaic Verse",
                "Frame Narratives: One Thousand and One Nights & Decameron",
                "Narratology: Diegetic Levels & Focalization (Genette)",
                "Unreliable Narration & Free Indirect Discourse",
                "Modernist Stream of Consciousness (Joyce, Woolf, Faulkner)",
                "World Literature, Postcolonial Voice & Hybridity",
                "Translation Theory: Dynamic vs Formal Equivalence"
            ),
            synopsis = "University comparative literature seminar covering oral epic traditions, frame narratives, Genette's narratology, modernism, postcolonial literature, and translation theory.",
            dedication = "Every translation is an act of hospitality between worlds.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Oral Epics & Frame Narrative Architecture",
                    "Ancient epics (The Epic of Gilgamesh, Iliad, Odyssey, Mahabharata, Ramayana) arose from oral performance using dactylic hexameter or sloka meter, epithets ('rosy-fingered dawn'), and ring composition. Framed collections—such as Scheherazade's One Thousand and One Nights, Panchatantra, and Boccaccio's Decameron—embed stories inside stories (mise en abyme), proving storytelling itself preserves human survival and cultural memory.",
                    "Narratology: Extradiegetic frame -> Intradiegetic tale -> Metadiegetic inset.",
                    "How does framing alter our trust in a story's moral conclusion?"
                ),
                BookPage(
                    2,
                    "2. Focalization, Unreliable Voice & Free Indirect Style",
                    "Gérard Genette distinguished 'who speaks' (voice) from 'who perceives' (focalization: zero/omniscient, internal, or external). Authors like Jane Austen pioneered Free Indirect Discourse—blending third-person grammar with a character's private idiom—while Nabokov, Ishiguro (The Remains of the Day), and Poe craft unreliable narrators whose omissions betray self-deception.",
                    "Technique: Free Indirect Discourse merges narrator authority with subjective bias.",
                    "Spot where Ishiguro's butler Stevens contradicts his own polished self-portrait."
                ),
                BookPage(
                    3,
                    "3. Stream of Consciousness & World Postcolonial Voices",
                    "Early 20th-century Modernism (Virginia Woolf's Mrs Dalloway, James Joyce's Ulysses, William Faulkner) abandoned linear plot clocks for Bergsonian psychological time (durée) and associative memory. Later 20th-century postcolonial classics (Chinua Achebe's Things Fall Apart, Gabriel García Márquez, Salman Rushdie) reclaimed epic voice, bilingual proverbs, and historical counter-narratives.",
                    "Key Terms: Intertextuality (Kristeva), Dialogism & Heteroglossia (Bakhtin).",
                    "How do proverbs in Achebe's prose bridge Igbo oral rhetoric and the English novel?"
                ),
                BookPage(
                    4,
                    "4. Translation Theory & Comparative Aesthetics",
                    "Every world canon exists across languages through translation. Walter Benjamin ('The Task of the Translator'), Eugene Nida (formal vs. dynamic equivalence), and Lawrence Venuti (domestication vs. foreignization) debate whether a translation should read invisibly like native prose or preserve the original culture's rhythmic and syntactic strangeness.",
                    "Dilemma: Literal semantic fidelity vs. prosodic/cultural resonance.",
                    "Compare two English translations of Bashō haiku or Homer via live Google Search."
                )
            )
        ),
        Book(
            id = "lit_pro",
            title = "Poetics, Hermeneutics & Craft",
            author = "Dr. Beatrice Moreau",
            category = BookCategory.LITERATURE,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 6,
            spineColor = Color(0xFF43101E),
            coverTrimColor = Color(0xFFF3CE7A),
            realSubjectScope = "Graduate Literary Theory, Hermeneutics, Structuralism & Post-Structuralism, Editorial Practice & Textual Criticism",
            completeTopics = listOf(
                "Philosophical Hermeneutics (Schleiermacher, Gadamer, Ricoeur)",
                "Russian Formalism (Defamiliarization / Ostranenie) & New Criticism",
                "Structuralism, Semiotics (Saussure, Barthes) & Narratological Codes",
                "Reader-Response Theory (Wolfgang Iser, Stanley Fish)",
                "Economy of Subtext, Prose Cadence & Parataxis vs Hypotaxis",
                "Developmental, Line & Copyediting Craft",
                "Textual Scholarship, Apparatus Criticus & Scholarly Editions"
            ),
            synopsis = "Graduate & editorial treatise uniting Gadamerian hermeneutics, Formalist/Reader-Response theory, prose stylistics, developmental editing, and archival textual criticism.",
            dedication = "For editors, critics, and lifelong stylists of the written word.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. The Hermeneutic Circle & Fusion of Horizons",
                    "From Friedrich Schleiermacher through Hans-Georg Gadamer's Truth and Method, hermeneutics rejects mechanical decoding: parts of a text only make sense within the whole work, while our grasp of the whole evolves through each sentence. Interpretation is a dialogic 'fusion of horizons' between historical authorial context and the reader's present situatedness.",
                    "Concept: Hermeneutic Circle + Horizon of Expectation (Jauss).",
                    "When re-reading a masterpiece years later, what changed—the text or your horizon?"
                ),
                BookPage(
                    2,
                    "§2. Formalism, Semiotics & Reader-Response Gaps",
                    "Viktor Shklovsky argued art exists to 'make the stone stony' via defamiliarization (ostranenie), slowing perception to renew wonder. Roland Barthes separated 'readerly' (passive) from 'writerly' texts, while Wolfgang Iser demonstrated that unwritten 'gaps' (Leerstellen) compel active co-creation by the reader's imagination.",
                    "Theoretical Lineage: Russian Formalism -> New Criticism -> Structuralism -> Reader-Response.",
                    "Identify one deliberate gap in a novel that deepens its emotional resonance."
                ),
                BookPage(
                    3,
                    "§3. Prose Architecture, Subtext & Editorial Restraint",
                    "Master stylists control sentence cadence via parataxis (short, punchy independent clauses creating urgency—Hemingway, McCarthy) versus hypotaxis (layered subordinate clauses mapping intricate reflection—Henry James, Proust). Subtext operates when surface dialogue masks unspoken power shifts; skilled developmental editing cuts explanatory scaffolding so scenes breathe on action and implication.",
                    "Editorial Lens: Macro (architecture/arc) -> Scene (turn/stakes) -> Line (cadence/verb energy).",
                    "Recast a flat explanatory sentence using concrete image and subtext."
                ),
                BookPage(
                    4,
                    "§4. Textual Criticism & Scholarly Apparatus",
                    "Before interpreting any historical classic, textual scholars collate surviving folios, quartos, manuscripts, and authorial proofs to reconstruct reliable copy-texts—documenting variants in an apparatus criticus rather than silently smoothing away ambiguity.",
                    "Practice: Collation -> Stemmatics -> Copy-Text Selection -> Annotated Apparatus.",
                    "Search real-time Google scholarship on famous textual variants in Shakespeare or Dickinson."
                )
            )
        ),

        // ================= STORY (Shelf 7) =================
        Book(
            id = "sto_school",
            title = "The Clockwork Fox & Star Map",
            author = "Oliver Finch",
            category = BookCategory.STORY,
            level = EducationLevel.SCHOOL,
            shelfNumber = 7,
            spineColor = Color(0xFFC7771A),
            coverTrimColor = Color(0xFFFFE09E),
            realSubjectScope = "Complete Adventure Story & World Folklore Craft: Fables, Hero's Quest Structure, Character Courage & Creative Storytelling",
            completeTopics = listOf(
                "Classic Adventure Story: Chapters 1–5 Complete Narrative",
                "Aesop, Grimm & Panchatantra World Fable Traditions",
                "The Young Hero's Journey (Call, Trials, Return)",
                "Dialogue Formatting & Showing Emotion Through Action",
                "World-Building Details: Maps, Artifacts & Sensory Anchors",
                "Writing Satisfying Beginnings & Memorable Endings"
            ),
            synopsis = "Complete illustrated adventure tale paired with world folklore traditions, hero's journey storytelling craft, dialogue rules, and creative writing tools.",
            dedication = "Keep a pocket compass and an open imagination.",
            pages = listOf(
                BookPage(
                    1,
                    "Chapter 1: Ticking Inside the Attic Globe",
                    "On a rainy Tuesday, eleven-year-old Pip noticed a quiet tick-tock pulsing from his grandmother's brass celestial globe. Pressing the tarnished Polaris star unlocked two hinged brass hemispheres—and out stepped a thumb-sized clockwork fox with amber glass eyes, a tiny velvet collar, and a rolled parchment map of lost bookshops.",
                    "Story Craft: Start with a concrete hook (sensory detail + mystery object).",
                    "What small curious sound or object launches your favorite adventure tale?"
                ),
                BookPage(
                    2,
                    "Chapter 2: The Map of Inked Constellations",
                    "The clockwork fox tapped a silver paw onto page twelve of Pip's sketchbook, and faint constellations rose like golden dust above the oak desk. In folklore across cultures—from Panchatantra talking beasts to Norse and Grimm tales—animal guides symbolize cleverness and test whether a traveler keeps promises under pressure.",
                    "Folklore Link: Trickster & Companion Archetypes across world mythology.",
                    "Name a famous animal guide or mentor from classic world stories."
                ),
                BookPage(
                    3,
                    "Chapter 3: The Trial of the Whispering Bridge",
                    "Crossing into the lantern-lit Night Market of Lost Titles, Pip faced three riddles guarding the Star Compass: one about patience, one about honesty, and one requiring him to return a stranger's dropped key instead of racing ahead. Hero stories hinge not on brute strength, but on moral choices made when nobody is watching.",
                    "Story Structure: Three-trial escalation tests the hero's heart before the climax.",
                    "Design a clever riddle whose answer relies on kindness rather than force."
                ),
                BookPage(
                    4,
                    "Chapter 4: Home Before Dawn & Storytelling Rules",
                    "As rain cleared into silver starlight, Pip placed the restored Star Compass gently back in the attic globe—discovering his own notebook now held blank maps ready for tomorrow. Every classic quest returns the hero home changed in courage and curiosity. Practice writing dialogue on fresh lines per speaker and ending scenes with forward momentum.",
                    "Checklist: Hook -> Inciting Call -> Escalating Trials -> Moral Climax -> Changed Return.",
                    "Explore classic adventure classics like 'The Hobbit' or 'Treasure Island' via live search!"
                )
            )
        ),
        Book(
            id = "sto_high",
            title = "Mystery at Midnight Station",
            author = "Arthur Hale",
            category = BookCategory.STORY,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 7,
            spineColor = Color(0xFFA85F12),
            coverTrimColor = Color(0xFFF5D084),
            realSubjectScope = "Detective & Mystery Fiction Mastery: Fair-Play Clue Design, Locked-Room Puzzles, Deductive Logic, Suspense & Golden Age Mystery Canon",
            completeTopics = listOf(
                "Complete Mystery Novella: Midnight Express Case",
                "Knox's Ten Commandments & Fair-Play Detective Rules",
                "Deductive vs Inductive vs Abductive Reasoning",
                "Clues, Red Herrings, Means, Motive & Opportunity",
                "Locked-Room Puzzles & Alibi Timeline Reconstruction",
                "Edgar Allan Poe, Arthur Conan Doyle & Agatha Christie Canon"
            ),
            synopsis = "Complete sleeper-train detective mystery combined with the real craft & literary history of mystery fiction from Poe and Sherlock Holmes to Agatha Christie.",
            dedication = "Every detail left on the table tells part of the truth.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Platform Nine: Crime Scene & Fair-Play Rules",
                    "Steam curled around brass lanterns as Inspector Lin boarded Sleeper Car Four—hours after a rare Gutenberg proof page vanished inside a snowbound express train. In classic 'Fair-Play' detective fiction (pioneered by Poe's Dupin, Arthur Conan Doyle's Sherlock Holmes, and Agatha Christie's Poirot), the author must show the reader every physical clue alongside the detective.",
                    "Clue Log #1: Torn emerald-wax receipt, damp boot heel by dining pantry, untouched emergency brake.",
                    "Why did Father Knox ban secret twin brothers and impossible poisons in fair-play mysteries?"
                ),
                BookPage(
                    2,
                    "2. Alibis, Red Herrings & Deductive Logic",
                    "Three passengers swore nobody left the parlor between 11:00 and 11:30 PM. Yet Inspector Lin noticed fresh melted slush ONLY inside the conductor's coat pocket and two coffee rings stacked slightly off-center on the dining-car leather menu. Mystery writers separate Deduction (guaranteed logical consequence) from Abduction (inference to the best explanation) while planting plausible Red Herrings to misdirect careless assumptions.",
                    "Detective Triangle: Means (access) + Motive (why) + Opportunity (timeline).",
                    "Spot the discrepancy between a witness timeline and physical melting snow!"
                ),
                BookPage(
                    3,
                    "3. The Locked-Room Solution & Revelation",
                    "Gently lifting the dining car's leather wine list revealed a false stitched pocket holding the missing folio intact. Nobody escaped through the snowed-shut window; the culprit forged footprints backward onto the vestibule ledge so suspicion would lock onto Car Four while the manuscript rode safely inside plain sight of dinner service.",
                    "Poe's 'Purloined Letter' Principle: The best hiding spot is often obvious everyday scenery.",
                    "Look up Agatha Christie's 'Murder on the Orient Express' clues in live Google Search!"
                ),
                BookPage(
                    4,
                    "4. Building Suspense, Foreshadowing & Pacing",
                    "True mystery suspense rises from ticking clocks (train arriving at border patrol), escalating personal stakes, and carefully calibrated foreshadowing planted chapters earlier—so the final reveal produces both genuine surprise and an immediate 'Wait, I saw that clue earlier!' recognition.",
                    "Mystery Craft: Plant early -> Conceal in plain noise -> Reveal via physical contradiction.",
                    "Outline a 3-clue mini mystery set inside a quiet night library."
                )
            )
        ),
        Book(
            id = "sto_uni",
            title = "Anthology of Quiet Horizons",
            author = "Various Authors",
            category = BookCategory.STORY,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 7,
            spineColor = Color(0xFF8C4B0C),
            coverTrimColor = Color(0xFFEAC270),
            realSubjectScope = "University Short Fiction Studies: Modern Short Story Craft (Chekhov, Joyce, Mansfield, O. Henry, Borges), Epiphany, Compression & Thematic Anthologies",
            completeTopics = listOf(
                "Anton Chekhov: Slice-of-Life, Subtext & 'Chekhov's Gun'",
                "James Joyce & Katherine Mansfield: The Psychological Epiphany",
                "Guy de Maupassant & O. Henry: Situational Irony & Structural Twist",
                "Jorge Luis Borges: Labyrinths, Mirrors & Metafiction",
                "Alice Munro & Raymond Carver: Compression & What Goes Unsaid",
                "Narrative Economy, Ending Resonance & Anthology Sequencing"
            ),
            synopsis = "University short-fiction anthology & masterclass dissecting Chekhovian subtext, Joycean epiphany, Borgesian metafiction, Carver minimalism, and twist mechanics.",
            dedication = "Small moments often hold our largest lives.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Chekhov, Subtext & Dramatic Economy",
                    "Anton Chekhov revolutionized modern short fiction by replacing melodramatic heroes with quiet human contradiction: people drink tea and discuss weather while their lives quietly turn. His famous maxim—'If a rifle hangs on the wall in chapter one, it must fire by chapter three'—teaches functional economy: every concrete detail must earn emotional or structural weight.",
                    "Canon Study: Chekhov's 'The Lady with the Dog' & 'Gooseberries'.",
                    "Identify a quiet conversation in life where real feelings lived underneath small talk."
                ),
                BookPage(
                    2,
                    "2. Epiphany & Compression (Joyce, Mansfield, Carver)",
                    "Unlike novels, short stories succeed through high compression—often illuminating an entire lifetime inside a single afternoon. James Joyce termed this sudden spiritual insight an 'epiphany' (as in 'The Dead' or 'Araby'), while Raymond Carver's minimalist craft uses iceberg omission so unvoiced grief echoes louder than speeches.",
                    "Craft Note: Ernest Hemingway's 'Iceberg Theory'—7/8 beneath the surface.",
                    "How can a brief 10-page story feel as spacious as a 300-page novel?"
                ),
                BookPage(
                    3,
                    "3. Borges, Metafiction & Library Labyrinths",
                    "Argentine master Jorge Luis Borges turned philosophy, encyclopedias, and infinite libraries ('The Library of Babel', 'The Garden of Forking Paths') into dazzling philosophical fictions—blurring essay and story to ask how language, memory, and chance construct reality itself.",
                    "Literary Mode: Magical Realism & Ficciones (Borges, García Márquez, Cortázar).",
                    "Read about Borges's infinite hexagonal library via live Google Search below!"
                ),
                BookPage(
                    4,
                    "4. The Keeper's Two Cups & Letters Never Mailed",
                    "Putting classical craft into practice: in our closing anthology pieces, a coastal lighthouse keeper always sets two mugs on stormy evenings, and thirty unmailed thank-you notes found inside a grandfather's books remind a reader to voice gratitude while people remain near enough to hear. Great endings linger not with fireworks, but with quiet moral gravity.",
                    "Anthology Arc: Opening premise -> Subtle motif echo -> Earned emotional openness.",
                    "Write down one quiet act of gratitude you don't want to leave unsent."
                )
            )
        ),
        Book(
            id = "sto_pro",
            title = "Architectures of Storytelling",
            author = "Eleanor Vance & Co.",
            category = BookCategory.STORY,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 7,
            spineColor = Color(0xFF6B3607),
            coverTrimColor = Color(0xFFF3CE7A),
            realSubjectScope = "Professional Novel, Screenwriting & Interactive Narrative Architecture: Three-Act/Kishōtenketsu Pacing, Scene-Sequel Structure, Agency & World-Building",
            completeTopics = listOf(
                "Narrative Promises, Progress & Earned Payoffs",
                "Three-Act Structure, Five-Act Drama & Four-Act Kishōtenketsu",
                "Dwight Swain's Scene-Sequel Rhythm (Goal-Conflict-Disaster)",
                "Psychic Distance, Deep POV & Multi-Thread Braiding",
                "Systemic World-Building Rules (Sanderson's Laws & Consistency)",
                "Revision Passes, Structural Diagnostics & Serialization"
            ),
            synopsis = "Master-level narrative design manual covering Kishōtenketsu & 3-Act structure, Scene-Sequel mechanics, psychic distance, systemic world-building, and revision craft.",
            dedication = "Craft serves wonder.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. Promise, Progress & Structural Geometries",
                    "An opening page signs a tonal contract with the reader—promising mystery, intimacy, or spectacle. Progress keeps promises visibly compounding until an inevitable yet surprising payoff. Alongside Western Three-Act escalation (Plot Point I -> Midpoint Reversal -> All-Is-Lost -> Climax), East Asian Kishōtenketsu (Ki introduction, Shō development, Ten unexpected perspective twist, Ketsu reconciliation) builds wonder without relying solely on violent conflict.",
                    "Frameworks: Aristotelian 3-Act | Kishōtenketsu (4-part twist) | Heroine's/Hero's Arc.",
                    "Compare a conflict-driven thriller midpoint with a Kishōtenketsu turn."
                ),
                BookPage(
                    2,
                    "§2. Scene-Sequel Rhythm & Character Agency",
                    "Dwight Swain formalized storytelling breath into active Scenes (Goal -> Escalating Conflict -> Setback/Disaster) paired with reflective Sequels (Emotional Reaction -> Dilemma -> New Decision). Passive protagonists whom events merely happen to stall momentum; unforgettable stories emerge when characters make painful value-driven choices under impossible pressure.",
                    "Rule of Agency: Each scene outcome ('Yes, but...' or 'No, and furthermore...') drives the next.",
                    "Diagnose a sagging middle chapter by checking whether every scene alters stakes."
                ),
                BookPage(
                    3,
                    "§3. Psychic Distance & Systemic World-Building",
                    "John Gardner's Psychic Distance slides seamlessly from distant cinematic panorama ('Five centuries ago the library stood...') down to raw visceral interiority ('Her thumb trembled on the latch'). In speculative fiction, rules must feel causal and bounded: limitations generate far better drama than limitless magic or technology.",
                    "Principle: Constraints + Human Cost > Arbitrary Spectacle.",
                    "Practice shifting one paragraph from Distant Overview to Close Deep POV."
                ),
                BookPage(
                    4,
                    "§4. Professional Revision & Diagnostic Passes",
                    "Professional authors revise in dedicated passes rather than polishing prose while plot skeletons remain broken: (1) Macro Structure & Pacing Pass, (2) Character Motivation & Arc Pass, (3) Scene Turning-Point & Dialogue Pass, (4) Sensory Texture & Line-Level Rhythm Pass.",
                    "Workflow: Structural Blueprint -> Zero Draft -> Diagnostic Passes -> Copy Edit.",
                    "Explore acclaimed craft manuals via live Google Search (e.g., McKee, Story Genius, Save the Cat)."
                )
            )
        ),

        // ================= COMIC (Shelf 8) =================
        Book(
            id = "com_school",
            title = "Captain Atom & The Paper Rocket",
            author = "Leo & Maya Studio",
            category = BookCategory.COMIC,
            level = EducationLevel.SCHOOL,
            shelfNumber = 8,
            spineColor = Color(0xFFD84343),
            coverTrimColor = Color(0xFFFFDF8E),
            realSubjectScope = "School Visual Storytelling & Science Comics: Panels, Speech Balloons, Onomatopoeia, Newtonian Rocket Physics & Comic Strip Creation",
            completeTopics = listOf(
                "Anatomy of a Comic Page: Panels, Tiers & Gutters",
                "Speech Balloons, Thought Clouds & Narrative Captions",
                "Onomatopoeia & Expressive Sound Effects (SFX)",
                "Action Poses, Silhouettes & Exaggerated Expressions",
                "Newton's Third Law & Rocket Thrust in Science Comics",
                "Creating a Complete 4-Panel Comic Strip"
            ),
            synopsis = "Complete school science-adventure comic paired with real sequential comic craft—teaching panels, speech balloons, action lines, and real rocket thrust physics!",
            dedication = "POW! ZAP! Curiosity takes flight!",
            pages = listOf(
                BookPage(
                    1,
                    "1. Panels, Balloons & Backyard Launch!",
                    "[PANEL 1 - WIDE] Sunlight hits a cardboard rocket taped in shiny silver foil on a backyard grass pad. Mia checks her clipboard: 'Fuel canisters locked! Camera rolling!' [PANEL 2 - CLOSE] Buddy the golden retriever wears giant blue swimming goggles: 'WOOF?!' [PANEL 3 - ACTION SPLASH] WHOOSH! Fizzing vinegar-and-baking-soda gas blasts downward—sending the rocket soaring! Comics arrange time inside boxes called PANELS separated by blank spaces called GUTTERS.",
                    "Comic Toolkit: Captions (Narrator) | Round Tails (Speech) | Jagged Borders (Shouts / Radio).",
                    "Read a strip and notice how panel width speeds up or slows down time!"
                ),
                BookPage(
                    2,
                    "2. Newton's Third Law & Gravity in Panels!",
                    "[PANEL 1] The paper rocket arcs high above the oak tree! [PANEL 2] Leo points upward: 'Wait—why did it curve back toward the lawn?' [PANEL 3] Mia sketches a chalk arrow: 'Action-Reaction! Gas shoots DOWN so thrust pushes UP—until Earth's gravity gently pulls our rocket home!' Great educational comics pair clear visual diagrams directly beside expressive characters.",
                    "Real Science: Thrust (CO₂ gas downward) vs. Weight (Gravity mg) + Air Drag.",
                    "Sketch or describe a 3-panel explanation of how parachutes slow landing!"
                ),
                BookPage(
                    3,
                    "3. Sound Effects, Motion Lines & Silhouettes",
                    "Because comics have no speakers, lettering BECOMES sound: tiny jagged 'tick-tick' conveys quiet tension, while bold tilted 'KA-BOOOOM!' shakes the whole panel frame. Clear character silhouettes make every jump, wave, or surprise instantly readable even before coloring.",
                    "Art Rule: Readability Check — fill your character solid black; is the pose still clear?",
                    "Invent three expressive sound effects for quiet library footsteps versus fireworks!"
                ),
                BookPage(
                    4,
                    "4. Build Your Own 4-Panel Comic Strip",
                    "Every memorable 4-panel strip (yonkoma or newspaper grid) follows four beats: Panel 1 Setup (introduce character + goal), Panel 2 Development (start action), Panel 3 Twist/Complication (unexpected surprise!), and Panel 4 Punchline/Payoff.",
                    "4-Beat Strip: Ki (Setup) -> Shō (Develop) -> Ten (Twist!) -> Ketsu (Punchline).",
                    "Look up famous STEM and adventure graphic novels for young creators using live search!"
                )
            )
        ),
        Book(
            id = "com_high",
            title = "Neon Skyline Chronicles",
            author = "Kenji Sato",
            category = BookCategory.COMIC,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 8,
            spineColor = Color(0xFFB52B2B),
            coverTrimColor = Color(0xFFF6CB78),
            realSubjectScope = "High School Graphic Novels & Visual Genre Craft: Chiaroscuro Contrast, Cyberpunk Worldbuilding, Pacing Splash Pages & Sequential Cinematography",
            completeTopics = listOf(
                "Issue #1–3: Neon Skyline Graphic Arc",
                "Chiaroscuro Ink Spotting & Limited Color Palettes",
                "Cyberpunk Themes: Technology, Memory & Urban Identity",
                "Splash Pages, Double-Page Spreads & Cliffhangers",
                "Show-Don't-Tell Visual Worldbuilding",
                "Dynamic Lettering Hierarchy & Pacing Beats"
            ),
            synopsis = "High-school cyberpunk graphic novel combined with complete instruction in ink spotting, chiaroscuro lighting, visual worldbuilding, splash pages, and lettering.",
            dedication = "Ink, rain, and electric light.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Issue #1 & Chiaroscuro Shadows",
                    "[SPLASH PANEL] Rain streaks across Sector 7 glass walkways, reflecting cyan hologram ads across dark puddles. Rooftop courier Sora grips a glowing analog cassette archive: 'If this master tape reaches the Broadcast Spire before midnight, the city remembers its real music.' Graphic novels use CHIAROSCURO—high-contrast pools of ink black against sharp neon highlights—to sculpt mood without heavy narration.",
                    "Ink Craft: Heavy feathering + 60/30/10 value hierarchy keeps busy cityscapes readable.",
                    "How does color contrast (warm amber vs cool cyan) separate hero from danger?"
                ),
                BookPage(
                    2,
                    "2. Action Choreography & Show-Don't-Tell",
                    "[3-PANEL TIER] Patrol drones sweep across rooftop steam -> Sora plants one boot on a rusted ventilation pipe -> mid-air leap across the neon canyon, trailing rain droplets! Instead of writing 'Sora was brave and athletic,' sequential artists show micro-decisions: shifting weight, tense hands, and motion lines crossing panel borders.",
                    "Visual Rule: Cross-gutter continuity aligns key action lines smoothly left-to-right.",
                    "Explore real cyberpunk classics like 'Akira' or 'Ghost in the Shell' via Google Search!"
                ),
                BookPage(
                    3,
                    "3. Page-Turn Cliffhangers & Issue Structure",
                    "Print and digital comics harness the physical PAGE TURN: place smaller tense buildup panels at the bottom-right corner of odd pages, then detonate the dramatic reveal full-width atop the very next page! An effective 22-page issue balances quiet character intimacy, kinetic action, and an irresistible final-page hook.",
                    "Page Rhythm: Odd right-hand pages hold cliffhanger surprises!",
                    "Notice how MR BOOK's own animated page flip mimics print page tension."
                )
            )
        ),
        Book(
            id = "com_uni",
            title = "Sequential Art & Visual Grammar",
            author = "Hannah Okafor",
            category = BookCategory.COMIC,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 8,
            spineColor = Color(0xFF911F1F),
            coverTrimColor = Color(0xFFE8BF6A),
            realSubjectScope = "University Comics Studies & Visual Semiotics: Scott McCloud's Closure & Iconography, Will Eisner's Sequential Art, Manga vs Franco-Belgian Traditions",
            completeTopics = listOf(
                "Scott McCloud's Six Panel-to-Panel Closure Transitions",
                "The Pyramid of Iconic Abstraction & Masking Effect",
                "Will Eisner: Time, Space & Framing as Language",
                "Word-Picture Combinations (Additive, Interdependent, Montage)",
                "Global Traditions: American, Franco-Belgian Ligne Claire & Japanese Manga",
                "Graphic Memoir & Nonfiction Journalism (Spiegelman, Satrapi, Sacco)"
            ),
            synopsis = "University comics studies textbook covering McCloud's closure taxonomy, Eisner's sequential grammar, masking iconography, global schools, and graphic journalism.",
            dedication = "Magic happens in the blank gutter between two panels.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Closure in the Gutter (McCloud's Six Transitions)",
                    "In Understanding Comics, Scott McCloud defined comics as 'juxtaposed pictorial and other images in deliberate sequence.' Closure occurs mentally inside the gutter between panels across six modes: (1) Moment-to-Moment (blink of an eye), (2) Action-to-Action (single subject acting), (3) Subject-to-Subject (scene focus shift), (4) Scene-to-Scene (jumping miles or years), (5) Aspect-to-Aspect (wandering mood across a place), and (6) Non-Sequitur.",
                    "Research Finding: American superheroes favor Action/Subject; Japanese manga uses far more Aspect-to-Aspect!",
                    "Analyze two consecutive panels in a manga or comic to classify their transition."
                ),
                BookPage(
                    2,
                    "2. Iconography, Masking & Word-Picture Braiding",
                    "The more simplified/iconic a face, the more universally readers project themselves inside it—allowing 'masking' (iconic stylized hero moving through hyper-detailed realistic backgrounds). Words and pictures interact across seven modes, peaking in Interdependent combinations where words and images together convey meaning neither conveys alone.",
                    "Key Scholars: Will Eisner ('Comics & Sequential Art'), Scott McCloud, Thierry Groensteen.",
                    "Why does placing an iconic cartoon face against textured photo-real scenery feel immersive?"
                ),
                BookPage(
                    3,
                    "3. Global Schools & Nonfiction Graphic Literature",
                    "Franco-Belgian Bande Dessinée (Hergé's Ligne Claire clean-line clarity, Moebius) prizes spacious architectural album pages; Japanese Manga pioneers dynamic decompression, speed lines, and cinematic pacing; modern graphic literature (Art Spiegelman's Maus, Marjane Satrapi's Persepolis, Joe Sacco) demonstrates comics' unmatched power for memoir, history, and investigative journalism.",
                    "Traditions: Ligne Claire (Clean Line) vs Manga Decompression vs Indie Graphic Memoir.",
                    "Search live Google scholarship on Thierry Groensteen's 'System of Comics'."
                )
            )
        ),
        Book(
            id = "com_pro",
            title = "Master Storyboarding & Layout",
            author = "Victor Alvarez",
            category = BookCategory.COMIC,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 8,
            spineColor = Color(0xFF701414),
            coverTrimColor = Color(0xFFF2CB75),
            realSubjectScope = "Professional Graphic Novel & Animation Storyboarding Production: Z-Path Eye-Tracking, Camera Staging, Value Thumbnailing, Script-to-Page Breakdown & Print Prep",
            completeTopics = listOf(
                "Z-Path Eye Flow, Tangents & Balloon Choreography",
                "Cinematographic Lenses, 180° Axis Rule & Camera Angles",
                "Thumbnailing, Notan & Two-Value Lighting Keys",
                "Full Script vs Marvel Plot-First Breakdowns",
                "Color Scripting, Atmospheric Perspective & Print CMYK",
                "Series Bibles, Model Sheets & Editorial Continuity"
            ),
            synopsis = "Studio-grade production handbook for working comic artists and storyboard directors covering eye-tracking geometry, camera blocking, tonal thumbnails, and CMYK print.",
            dedication = "Guide the eye, then surprise the heart.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. Eye-Tracking Geometry, Tangents & Lettering Flow",
                    "Western readers scan spreads along a natural Z-path (top-left to bottom-right; manga reverses right-to-left). Master layout directors stage speech balloons in upper corners so captions lead the eye smoothly into character faces and action vectors—relentlessly hunting down accidental visual tangents that flatten depth or split attention.",
                    "Production Rule: Leave 25–30% breathing room inside every dialogue-heavy panel before lettering!",
                    "Audit a busy layout: does the eye jump immediately to balloon #1 -> face -> action exit?"
                ),
                BookPage(
                    2,
                    "§2. Camera Blocking, 180° Rule & Staging",
                    "Like film storyboarding, comics obey the 180° axis of action so opposing speakers stay consistently on left/right sides across cuts unless intentionally crossing the line via a neutral bridge shot. Extreme wide shots establish geography; Dutch angles destabilize certainty; over-the-shoulder closeups anchor emotional intimacy.",
                    "Shot Ladder: Establishing Wide -> Medium Two-Shot -> Reaction Close-Up -> Insert Detail.",
                    "Notice how MR BOOK transitions smoothly from Hall Wide View down to intimate Table POV!"
                ),
                BookPage(
                    3,
                    "§3. Thumbnails, Value Keys & Pre-Press Production",
                    "Before penciling crisp lines, professionals iterate tiny 2-inch thumbnails purely in three tonal values (black, mid-gray, white) to lock strong readable compositions. Final artwork separates flatting, atmospheric lighting layers, CMYK ink-limit safety (<= 300% total ink coverage for offset print), and bleed/trim margins.",
                    "Print Specs: 300–600 DPI final art | 0.125 in bleed | CMYK safe gamut check.",
                    "Search live Google resources for modern comic & animation storyboarding studios."
                )
            )
        )
    )
}
