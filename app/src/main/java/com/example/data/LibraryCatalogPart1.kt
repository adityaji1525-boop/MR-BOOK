package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.BookPage
import com.example.model.EducationLevel

internal object LibraryCatalogPart1 {
    val books: List<Book> = listOf(
        // ================= SCIENCE (Shelves 1–2) =================
        Book(
            id = "sci_school",
            title = "Why the Sky Glows Blue",
            author = "Dr. Mira Patel",
            category = BookCategory.SCIENCE,
            level = EducationLevel.SCHOOL,
            shelfNumber = 1,
            spineColor = Color(0xFF1D6F82),
            coverTrimColor = Color(0xFFD9B26F),
            realSubjectScope = "Elementary Atmospheric Physics, Solar Spectrum, Light Scattering, Optics & Earth's Atmosphere",
            completeTopics = listOf(
                "Electromagnetic & Visible Light Spectrum",
                "Composition of Earth's Atmosphere (N₂, O₂, Ar)",
                "Rayleigh Scattering & Wavelength Dependence",
                "Why Clouds Appear White (Mie Scattering)",
                "Red & Amber Sunsets (Atmospheric Path Length)",
                "Refraction, Dispersion & Rainbows",
                "Atmospheric Layers & Sky Brightness"
            ),
            synopsis = "Complete foundational guide to atmospheric optics: the visible light spectrum, Earth's gas composition, Rayleigh & Mie scattering, rainbow dispersion, and sunset physics.",
            dedication = "For young observers who look upward and ask why.",
            pages = listOf(
                BookPage(
                    1,
                    "1. The Visible Light Spectrum & Sunlight",
                    "Sunlight travels ~150 million kilometers from the Sun to Earth as electromagnetic radiation. What our eyes perceive as white light is actually a continuous spectrum of wavelengths ranging from roughly 380 nm (violet) to 700 nm (red). Short-wavelength violet and blue photons oscillate rapidly, while long-wavelength orange and red photons carry gentler spatial oscillations.",
                    "Spectrum Data: Violet 380–450 nm | Blue 450–495 nm | Green 495–570 nm | Red 620–750 nm.",
                    "What color bands can you spot when sunlight passes through a glass prism or bevel?"
                ),
                BookPage(
                    2,
                    "2. Atmospheric Composition & Rayleigh Scattering",
                    "Earth's dry lower atmosphere consists of about 78% nitrogen (N₂), 21% oxygen (O₂), and 1% argon and trace gases. Because these molecules are much smaller than the wavelength of visible light, they trigger Rayleigh scattering, whose intensity scales inversely with the fourth power of wavelength (I ∝ 1/λ⁴). Blue light (~450 nm) is scattered roughly 5 to 6 times more strongly than red light (~680 nm), filling the daytime sky with blue from every direction.",
                    "Law: Rayleigh Scattering Intensity ∝ 1 / λ⁴.",
                    "Why doesn't the sky look pure violet if violet scatters even more? Check solar output + cone sensitivity!"
                ),
                BookPage(
                    3,
                    "3. White Clouds (Mie Scattering) & Rainbows",
                    "Unlike tiny nitrogen molecules, liquid water droplets and ice crystals inside clouds have diameters comparable to or larger than visible wavelengths (micrometers). They scatter all visible wavelengths nearly equally via Mie scattering, making clouds look crisp white (or dark gray when thick clouds absorb/block light below). Rainbows form when sunlight enters spherical raindrops, refracts, reflects internally off the back wall, and disperses colors at ~42° (red) to ~40° (violet).",
                    "Angles: Primary Rainbow Red 42°, Violet 40° from the antisolar point.",
                    "Where must the Sun be relative to your back when spotting a rainbow?"
                ),
                BookPage(
                    4,
                    "4. Sunsets, Atmospheric Path Length & Layers",
                    "At noon, sunlight crosses roughly 10–15 km of dense troposphere vertically. Near sunset, light skims horizontally through several times more air mass and dust aerosols. Most short blue and green wavelengths scatter sideways before reaching your eyes, leaving predominantly amber, orange, and crimson rays along the western horizon.",
                    "Atmosphere Layers: Troposphere (0–12 km), Stratosphere (12–50 km), Mesosphere, Thermosphere, Exosphere.",
                    "Compare sunset colors after clear air versus dusty or volcanic evenings."
                ),
                BookPage(
                    5,
                    "5. Scientific Method & Sky Observation Craft",
                    "Observational science records controlled variables: solar elevation angle, cloud cover oktas, humidity, and air quality index (AQI). Keeping an illustrated sky journal connects everyday weather patterns to quantitative atmospheric physics.",
                    "Field Protocol: Log time, solar altitude, cloud genus (cumulus, cirrus, stratus), and color gradient.",
                    "Connect this page to real-time Google Search to inspect today's live atmospheric optics or space weather."
                )
            )
        ),
        Book(
            id = "sci_high",
            title = "Waves, Motion & Gravity",
            author = "Prof. Henrik Lindqvist",
            category = BookCategory.SCIENCE,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 1,
            spineColor = Color(0xFF155666),
            coverTrimColor = Color(0xFFE0BA77),
            realSubjectScope = "Classical Mechanics, Newton's Laws, Universal Gravitation, Energy Conservation, Mechanical & Electromagnetic Waves, Thermodynamics",
            completeTopics = listOf(
                "Kinematics: Displacement, Velocity & Acceleration",
                "Newton's Three Laws of Motion & Free-Body Diagrams",
                "Work, Kinetic/Potential Energy & Conservation Laws",
                "Linear Momentum, Impulse & Elastic/Inelastic Collisions",
                "Newton's Law of Universal Gravitation & Keplerian Orbits",
                "Simple Harmonic Motion, Standing Waves & Doppler Effect",
                "Electromagnetic Spectrum, Interference & Diffraction",
                "Laws of Thermodynamics & Heat Engines"
            ),
            synopsis = "Complete high-school physics textbook covering Newtonian mechanics, momentum, universal gravitation, orbital dynamics, wave optics, and thermodynamics.",
            dedication = "To every student sketching free-body arrows in the margins.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Kinematics & Newton's Laws of Motion",
                    "Kinematics describes motion via position x(t), velocity v = dx/dt, and constant-acceleration equations such as v² = v₀² + 2aΔx. Newton's First Law defines inertial frames; the Second Law links net external force to acceleration (ΣF = ma); the Third Law establishes equal and opposite action-reaction pairs acting on distinct bodies. Even a book resting on an oak desk obeys ΣF = 0 between gravity (mg) and the upward normal contact force.",
                    "Core Equations: F_net = m·a | Δx = v₀t + ½at² | f_k = μ_k·N.",
                    "Draw a mental free-body diagram of a sled sliding down an inclined plane."
                ),
                BookPage(
                    2,
                    "2. Work, Energy Conservation & Momentum",
                    "Mechanical work W = F·d·cos(θ) transfers energy into kinetic energy (K = ½mv²) or potential stores such as gravitational (U_g = mgh) and elastic spring energy (U_s = ½kx²). When external net force is zero, total linear momentum p = mv stays strictly conserved across explosions and collisions—elastic collisions preserve kinetic energy while inelastic ones convert part into thermal or deformational energy.",
                    "Conservation: ΔK + ΔU + ΔE_thermal = W_ext | Σp_initial = Σp_final.",
                    "Why are car crumple zones designed to increase collision duration Δt?"
                ),
                BookPage(
                    3,
                    "3. Universal Gravitation & Orbital Mechanics",
                    "Newton united terrestrial falling with celestial motion: F = G·M·m / r² (where G ≈ 6.674×10⁻¹¹ N·m²/kg²). Setting gravitational force equal to centripetal force m·v²/r yields circular orbital speed v = sqrt(GM/r) and Kepler's Third Law T² = (4π²/GM)·a³. Satellites orbit because their tangential velocity causes them to fall continuously around Earth's curvature.",
                    "Escape Speed: v_esc = sqrt(2GM / R) ≈ 11.2 km/s from Earth's surface.",
                    "Compare Low Earth Orbit altitude (~400 km) with Geostationary Orbit (~35,786 km)."
                ),
                BookPage(
                    4,
                    "4. Simple Harmonic Motion & Wave Physics",
                    "Restoring forces proportional to displacement (F = -kx) produce sinusoidal oscillations with period T = 2π·sqrt(m/k) for springs and T = 2π·sqrt(L/g) for small-angle pendulums. Waves transport energy via medium oscillation (transverse/longitudinal) obeying v = f·λ. Superposition creates standing waves, beats, constructive interference (Δr = nλ), and Doppler frequency shifts for moving sources.",
                    "Wave Relation: v = λ · f | Sound in 20°C Air ≈ 343 m/s | Light c ≈ 3.00×10⁸ m/s.",
                    "Why do musical instruments produce distinct harmonic overtones?"
                ),
                BookPage(
                    5,
                    "5. Thermodynamics & Entropy Foundations",
                    "Thermodynamics governs heat Q, work W, and internal energy U. The First Law (ΔU = Q - W) conserves energy; the Second Law states that total entropy of an isolated system never decreases (ΔS ≥ 0), capping ideal Carnot heat engine efficiency at η = 1 - T_cold / T_hot (using Kelvin temperatures).",
                    "Entropy & Efficiency: ΔS = ∫ dQ_rev / T | η_max = 1 - T_c / T_h.",
                    "Why is a 100% efficient cyclical heat engine physically impossible?"
                )
            )
        ),
        Book(
            id = "sci_uni",
            title = "Cellular Symphonies & Ecology",
            author = "Dr. Elena Rostova",
            category = BookCategory.SCIENCE,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 2,
            spineColor = Color(0xFF237A6E),
            coverTrimColor = Color(0xFFD6B46E),
            realSubjectScope = "University Cell Biology, Biochemistry, Molecular Genetics, Evolutionary Population Dynamics & Ecosystem Ecology",
            completeTopics = listOf(
                "Fluid Mosaic Membrane & Electrochemical Transport",
                "Cellular Respiration: Glycolysis, Krebs Cycle & Oxidative Phosphorylation",
                "Photosynthesis: Light Reactions & Calvin-Benson Cycle",
                "DNA Replication, Proofreading & Telomere Biology",
                "Central Dogma: Transcription, Splicing & Ribosomal Translation",
                "Gene Regulation, Epigenetics & CRISPR Mechanisms",
                "Hardy-Weinberg Equilibrium & Natural Selection",
                "Ecosystem Biogeochemistry & Mycorrhizal Networks"
            ),
            synopsis = "Comprehensive university biology covering membrane biophysics, metabolic pathways, molecular genetics, evolutionary theory, and ecosystem symbioses.",
            dedication = "Dedicated to life's quiet resilience at every scale.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Membranes, Osmosis & Ion Gradients",
                    "Biological membranes follow the Fluid Mosaic Model: amphipathic phospholipids self-assemble into a selectively permeable bilayer studded with integral channel, carrier, and receptor proteins. Active pumps such as the Na⁺/K⁺-ATPase hydrolyze ATP to export 3 Na⁺ and import 2 K⁺, maintaining resting membrane potentials (~ -70 mV in neurons) that drive action potentials and secondary cotransport.",
                    "Thermodynamics: ΔG = RT ln(C_in/C_out) + zFΔΨ.",
                    "How does cholesterol buffer membrane fluidity across temperature shifts?"
                ),
                BookPage(
                    2,
                    "2. Bioenergetics: ATP, Respiration & Photosynthesis",
                    "Heterotrophs oxidize glucose via cytosolic glycolysis (net 2 ATP + 2 NADH), mitochondrial pyruvate oxidation, the Citric Acid Cycle (generating NADH/FADH₂), and the electron transport chain + ATP synthase (~30–32 ATP total per glucose, using O₂ as terminal acceptor). Autotrophs reverse this redox flow in chloroplasts: Photosystems II & I split H₂O to make ATP and NADPH, fueling Rubisco carbon fixation in the Calvin cycle.",
                    "Overall Respiration: C₆H₁₂O₆ + 6 O₂ -> 6 CO₂ + 6 H₂O + ~30–32 ATP.",
                    "Compare substrate-level phosphorylation with chemiosmotic oxidative phosphorylation."
                ),
                BookPage(
                    3,
                    "3. DNA Replication & The Central Dogma",
                    "DNA polymerases synthesize 5'->3' semiconservatively using leading-strand continuity and lagging-strand Okazaki fragments primed by RNA primase. RNA polymerase transcribes protein-coding genes into pre-mRNA, which receives a 5' m⁷G cap, 3' poly-A tail, and spliceosomal intron excision before ribosomes decode codons (AUG start; UAA/UAG/UGA stop) using aminoacyl-tRNAs.",
                    "Pathway: DNA --(Transcription)--> mRNA --(Translation)--> Folding Protein.",
                    "Why does 3'->5' exonuclease proofreading dramatically cut mutation rates?"
                ),
                BookPage(
                    4,
                    "4. Gene Regulation, Epigenetics & CRISPR",
                    "Every somatic cell carries an identical genome yet specializes through transcription factors, enhancers, chromatin remodeling (histone acetylation loosens euchromatin; CpG methylation silences promoters), and non-coding microRNAs. In prokaryotes, adaptive immunity via CRISPR-Cas9 uses guide RNAs to target double-stranded breaks at complementary sequences.",
                    "Regulation Layers: Chromatin -> Transcriptional -> Post-transcriptional -> Post-translational.",
                    "Explore live recent clinical CRISPR milestones using the Google Search panel below."
                ),
                BookPage(
                    5,
                    "5. Population Genetics & Forest Mycorrhizal Webs",
                    "Evolution shifts allele frequencies (p + q = 1; p² + 2pq + q² = 1 under Hardy-Weinberg null assumptions) via selection, genetic drift, gene flow, and mutation. At ecosystem scale, 80–90% of terrestrial plants form arbuscular or ectomycorrhizal fungal symbioses—trading photosynthesized carbon for soil phosphorus and nitrogen across interconnected trophic webs.",
                    "Ecology Metric: Gross Primary Productivity (GPP) vs Net Primary Productivity (NPP = GPP - R).",
                    "How do mycorrhizal networks redistribute nutrients across shaded forest understories?"
                )
            )
        ),
        Book(
            id = "sci_pro",
            title = "Quantum Fields & Research Craft",
            author = "Dr. Julian Vance",
            category = BookCategory.SCIENCE,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 2,
            spineColor = Color(0xFF0E404C),
            coverTrimColor = Color(0xFFF0C76B),
            realSubjectScope = "Quantum Mechanics, Quantum Field Theory, Gauge Symmetries, Quantum Electrodynamics, Experimental Methodology & Statistical Inference",
            completeTopics = listOf(
                "Hilbert Spaces, Operators & Schrödinger Dynamics",
                "Second Quantization & Relativistic Klein-Gordon / Dirac Fields",
                "Noether's Theorem & Continuous Global Symmetries",
                "Local U(1) Gauge Invariance & Quantum Electrodynamics (QED)",
                "Feynman Diagrams, Perturbation Theory & Renormalization",
                "Standard Model SU(3)×SU(2)×U(1) & Higgs Mechanism",
                "Experimental Epistemology, Statistical Power & Bias Control",
                "Pre-Registration, Error Propagation & Open Science"
            ),
            synopsis = "Graduate & research-level reference on canonical quantization, Noether's theorem, QED gauge symmetry, renormalization, and rigorous experimental design.",
            dedication = "For researchers comfortable standing at the edge of the known.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. From Wavefunctions to Quantum Fields",
                    "Non-relativistic quantum mechanics evolves state vectors |ψ(t)⟩ in Hilbert space via iħ ∂|ψ⟩/∂t = Ĥ|ψ⟩, constrained by canonical commutators [x̂, p̂] = iħ. When combined with Special Relativity, particle number ceases to be conserved (E = mc² permits pair creation), requiring Second Quantization: fields φ(x) and conjugate momenta π(x) become operators whose excitations define discrete particle quanta.",
                    "Canonical Quantization: [φ(x), π(y)] = iħ δ³(x - y) | {ψ_a(x), ψ†_b(y)} = δ_ab δ³(x - y).",
                    "Why does causality demand spacelike field operators commute or anticommute?"
                ),
                BookPage(
                    2,
                    "§2. Noether's Theorem & Gauge Symmetries",
                    "Emmy Noether proved that every differentiable symmetry of the action S = ∫ ℒ d⁴x yields a conserved current ∂_μ J^μ = 0. Time translation gives energy, spatial translation gives momentum, and rotation gives angular momentum. Demanding local phase invariance ψ(x) -> e^{iα(x)}ψ(x) forces introduction of a covariant derivative D_μ = ∂_μ + iqA_μ and massless spin-1 gauge boson—deriving electromagnetism directly from U(1) symmetry.",
                    "QED Lagrangian: ℒ_QED = ψ̄(iγ^μ D_μ - m)ψ - ¼ F_μν F^μν.",
                    "Compare global internal symmetries (charge conservation) with local gauge invariance."
                ),
                BookPage(
                    3,
                    "§3. Perturbation Theory, Loops & Renormalization",
                    "Because QED's fine-structure coupling α ≈ 1/137 is small, scattering amplitudes expand reliably via Feynman diagrams summing tree-level and virtual loop paths. UV divergences inside loop integrals are absorbed systematically into running physical mass and charge parameters via renormalization group equations—predicting the electron's anomalous magnetic moment g-2 to >12 decimal places.",
                    "Running Coupling: α(q²) increases slowly at higher momentum transfer (vacuum polarization screening).",
                    "Search live Google updates for the latest Fermilab Muon g-2 or CERN LHC experimental results."
                ),
                BookPage(
                    4,
                    "§4. Experimental Rigor, Power & Systematic Bias",
                    "High-precision discovery demands guarding against systematic drift, selection bias, look-elsewhere effects, and p-hacking. Sound protocols define primary endpoints, calculate statistical power (1 - β) against realistic effect sizes prior to data collection, apply blinding during analysis, and propagate both aleatoric (statistical) and epistemic (systematic) uncertainties cleanly.",
                    "Discovery Thresholds: 5σ significance in particle physics (p ≈ 3×10⁻⁷) vs standard α = 0.05.",
                    "Identify one confounding variable in a study design and plan how randomization eliminates it."
                )
            )
        ),

        // ================= TECHNOLOGY (Shelves 3–4) =================
        Book(
            id = "tech_school",
            title = "Hello World: First Steps in Code",
            author = "Samira Chen",
            category = BookCategory.TECHNOLOGY,
            level = EducationLevel.SCHOOL,
            shelfNumber = 3,
            spineColor = Color(0xFF3949AB),
            coverTrimColor = Color(0xFFF3C669),
            realSubjectScope = "Foundational Computer Science: Binary Logic, Algorithms, Control Flow, Functions, Debugging & How Computers Work",
            completeTopics = listOf(
                "Binary Numbers, Bits, Bytes & ASCII/UTF-8 Encoding",
                "Computer Hardware: CPU, RAM, Storage & I/O",
                "Algorithmic Thinking: Sequence & Decomposition",
                "Variables, Data Types (Int, String, Boolean)",
                "Conditionals (If/Else) & Boolean Logic (AND, OR, NOT)",
                "Loops (For/While) & Functions / Parameters",
                "Debugging Strategies & Edge-Case Testing",
                "How the Internet Works (IP, DNS & Safety)"
            ),
            synopsis = "Complete school computer science starter covering binary & UTF-8, CPU/RAM architecture, algorithms, variables, loops, functions, debugging, and internet basics.",
            dedication = "To young builders turning curiosity into instructions.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Bits, Bytes & Computer Hardware",
                    "Inside every phone or laptop, billions of microscopic transistors flip between OFF (0) and ON (1)—called bits. Eight bits form 1 byte (256 combinations), enough to encode letters (ASCII/UTF-8), numbers, and pixel colors. The CPU fetches and executes instructions from fast temporary RAM, while SSDs keep files saved when power turns off.",
                    "Binary Place Values: 1010₂ = 8 + 0 + 2 + 0 = 10₁₀ | 1 KB = 1,024 B (KiB) or 1,000 B.",
                    "Convert your age or favorite number into 8-bit binary!"
                ),
                BookPage(
                    2,
                    "2. Algorithms, Variables & Control Flow",
                    "An algorithm is an unambiguous finite sequence of steps to solve a problem. Programs store changing values in named variables (such as score: Int or title: String), make decisions with Boolean conditionals (if / else using ==, <, &&, ||), and automate repetition with bounded for-loops and condition-driven while-loops.",
                    "Control Example: for (step in 1..5) { if (hasNextPage()) turnPage() }",
                    "Trace what happens if a while-loop forgets to update its counter!"
                ),
                BookPage(
                    3,
                    "3. Functions & Modular Decomposition",
                    "Instead of copying the same 20 lines repeatedly, engineers package reusable steps into named functions taking parameters and returning outputs. Clean decomposition makes large programs readable, testable, and easy for teammates to extend.",
                    "Function Signature: fun areaOfRectangle(width: Int, height: Int): Int = width * height",
                    "Think of three mini-functions that could power an alarm clock app."
                ),
                BookPage(
                    4,
                    "4. Debugging, Edge Cases & Internet Basics",
                    "Syntax errors break grammar before running; logic bugs produce wrong answers while running. Test boundary inputs (0, empty text, huge numbers) and trace variable values line-by-line. Online, Domain Name System (DNS) translates readable names into numeric IP addresses routed in small packets via HTTPS encryption.",
                    "Checklist: Null/Empty input -> Boundary min/max -> Expected normal path.",
                    "Search live Google info on how DNS resolves a website address in milliseconds."
                )
            )
        ),
        Book(
            id = "tech_high",
            title = "Data Structures & Clean Logic",
            author = "Marcus Thorne",
            category = BookCategory.TECHNOLOGY,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 3,
            spineColor = Color(0xFF283593),
            coverTrimColor = Color(0xFFE0B45A),
            realSubjectScope = "High School / AP Computer Science: Asymptotic Big-O Complexity, Linear Structures, Hash Tables, Trees, Recursion & Graph Algorithms",
            completeTopics = listOf(
                "Asymptotic Complexity: Big-O, Ω & Θ Analysis",
                "Arrays, Dynamic Arrays & Singly/Doubly Linked Lists",
                "Stacks (LIFO), Queues (FIFO) & Priority Heaps",
                "Hash Tables, Hash Functions & Collision Resolution",
                "Recursion, Divide-and-Conquer & Binary Search",
                "Binary Search Trees (BST), AVL & Trie Structures",
                "Graph Representations (Adjacency List vs Matrix)",
                "BFS, DFS & Dijkstra's Shortest Path Algorithm"
            ),
            synopsis = "Complete algorithms & data structures handbook covering Big-O notation, arrays, linked lists, stacks/queues, hash maps, trees, heaps, and graph traversal.",
            dedication = "Organized shelves make fast libraries—and fast software.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Asymptotic Complexity & Linear Containers",
                    "Big-O measures worst-case growth as input N scales: O(1) constant, O(log N) logarithmic, O(N) linear, O(N log N) linearithmic, and O(N²) quadratic. Contiguous arrays give O(1) random indexing by memory offset but O(N) middle insertion; linked lists trade O(1) pointer splice insertion for O(N) sequential search.",
                    "Benchmarks: Dynamic Array append = O(1) amortized | Array index = O(1).",
                    "Why can O(N log N) beat O(N²) by a factor of 50,000 at N = 1,000,000?"
                ),
                BookPage(
                    2,
                    "2. Stacks, Queues & Hash Tables",
                    "Stacks enforce Last-In First-Out (push/pop) for function call frames and undo stacks; Queues enforce First-In First-Out (enqueue/dequeue) for schedulers and BFS. Hash Tables map keys via a hash function h(key) % capacity to achieve average O(1) insert/lookup, resolving collisions via separate chaining or open addressing while resizing at high load factor.",
                    "Operations: Stack/Queue = O(1) | HashMap avg get/put = O(1), worst = O(N).",
                    "What makes a cryptographic or hash table distribution function uniform?"
                ),
                BookPage(
                    3,
                    "3. Recursion, Binary Search & Sorting",
                    "Divide-and-conquer splits problems into smaller self-similar subproblems until hitting a base case. Binary search halves a sorted array each step for O(log N) lookup. Merge Sort and Quick Sort achieve O(N log N) average sorting; Merge Sort guarantees O(N log N) worst-case stability using O(N) auxiliary memory.",
                    "Recurrence: T(N) = 2T(N/2) + O(N) => O(N log N).",
                    "Always verify your base case before writing recursive calls!"
                ),
                BookPage(
                    4,
                    "4. Trees, Priority Heaps & Tries",
                    "Balanced Binary Search Trees keep height O(log N) for ordered range queries. Binary Heaps maintain max-or-min parent ordering in an array for O(log N) priority extraction (used in task schedulers). Tries (prefix trees) index strings character-by-character so autocomplete takes O(L) time independent of dictionary size.",
                    "Heap Indices: left = 2i + 1 | right = 2i + 2 | parent = (i - 1) / 2.",
                    "Why does MR BOOK's search benefit from prefix indexing and filtering?"
                ),
                BookPage(
                    5,
                    "5. Graphs: BFS, DFS & Dijkstra's Algorithm",
                    "Graphs model networks of vertices V and edges E, commonly stored via sparse Adjacency Lists O(V + E). Breadth-First Search (Queue) finds minimum-hop shortest paths in unweighted graphs; Depth-First Search (Stack/Recursion) detects cycles and topological orderings; Dijkstra's priority-queue relaxation solves non-negative weighted shortest paths in O((V + E) log V).",
                    "Complexity: BFS & DFS = O(V + E) | Dijkstra = O((V + E) log V).",
                    "Trace BFS versus DFS from the Library Centre to Shelf #10."
                )
            )
        ),
        Book(
            id = "tech_uni",
            title = "Compilers, Networks & AI Craft",
            author = "Dr. Aris Thorne",
            category = BookCategory.TECHNOLOGY,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 4,
            spineColor = Color(0xFF1E2B7A),
            coverTrimColor = Color(0xFFE8C168),
            realSubjectScope = "University Computer Engineering: Compiler Design, Operating Systems, Computer Networking (TCP/IP) & Modern Deep Learning / Transformers",
            completeTopics = listOf(
                "Lexical Scanning (DFA/NFA) & Context-Free Parsing",
                "Abstract Syntax Trees, Type Checking & SSA IR",
                "Register Allocation & Machine Code Optimization",
                "OS Processes, Threads, Virtual Memory & Mutex Locks",
                "OSI / TCP-IP Layers, Congestion Control & TLS 1.3",
                "HTTP/2, QUIC (HTTP/3) & Low-Latency Transport",
                "Gradient Descent, Backpropagation & Regularization",
                "Self-Attention, Transformers & Modern LLM Inference"
            ),
            synopsis = "University CS engineering manual uniting compiler pipelines, OS concurrency & virtual memory, TCP/IP & QUIC networking, and Transformer deep learning architectures.",
            dedication = "For architects bridging human intent and silicon precision.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Compiler Frontend & Static Single Assignment",
                    "Compilers convert source text via Lexical Analysis (regular expressions -> finite automata -> tokens), Parsing (LL/LR grammars -> Abstract Syntax Tree), and Semantic Type Checking. Modern middle-ends (LLVM) lower code into Static Single Assignment (SSA) Intermediate Representation—where every variable is assigned exactly once—simplifying dead-code elimination, constant folding, and loop invariant hoisting.",
                    "Pipeline: Source -> Lexer -> Parser (AST) -> Type Checker -> SSA IR -> Register Allocator -> Machine Code.",
                    "Why does SSA form simplify dataflow dependency analysis?"
                ),
                BookPage(
                    2,
                    "2. Operating Systems: Threads, Locks & Virtual Memory",
                    "Operating systems virtualize hardware via isolated process address spaces backed by multi-level Page Tables and hardware Translation Lookaside Buffers (TLBs). Multi-threaded concurrency shares memory, demanding synchronization primitives (atomic CAS, mutexes, semaphores) while avoiding deadlocks (circular wait, hold-and-wait) and data races.",
                    "Page Fault Flow: Virtual Addr -> TLB Miss -> Page Table Walk -> Disk Swap / Zero-Fill.",
                    "How do Kotlin Coroutines multiplex thousands of tasks onto small thread pools?"
                ),
                BookPage(
                    3,
                    "3. Computer Networks: TCP, Congestion & QUIC",
                    "The Internet Protocol (IP) provides best-effort packet routing across autonomous systems via BGP. TCP layers reliable in-order byte streams atop IP using 3-way SYN/ACK handshakes, cumulative ACKs, flow control windows, and congestion control (AIMD / CUBIC / BBR). QUIC (HTTP/3) moves multiplexing and TLS 1.3 encryption onto UDP to eliminate head-of-line blocking.",
                    "Packet Latency = Propagation + Transmission + Queuing + Processing.",
                    "Search live Google documentation on HTTP/3 and TLS 1.3 0-RTT handshakes."
                ),
                BookPage(
                    4,
                    "4. Deep Learning, Backpropagation & Transformers",
                    "Neural networks optimize parameterized functions by computing loss gradients via reverse-mode automatic differentiation (backpropagation) and AdamW updates. Transformers replaced sequential recurrence with scaled dot-product Self-Attention: Attention(Q, K, V) = softmax(QKᵀ / √d_k)V, capturing long-range dependencies in parallel across massive corpora.",
                    "Core Formula: Attention(Q, K, V) = softmax((Q · Kᵀ) / √d_k) · V.",
                    "How does KV-caching accelerate autoregressive token generation?"
                )
            )
        ),
        Book(
            id = "tech_pro",
            title = "Distributed Systems Architecture",
            author = "Nadia Kowalski",
            category = BookCategory.TECHNOLOGY,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 4,
            spineColor = Color(0xFF151E54),
            coverTrimColor = Color(0xFFF0C970),
            realSubjectScope = "Staff/Principal Distributed Engineering: Logical Time, Consensus (Raft/Paxos), Replication, Partitioning, Resilience & Observability",
            completeTopics = listOf(
                "Physical Clock Skew, Lamport Timestamps & Vector Clocks",
                "CAP & PACELC Theorems (Consistency vs Latency)",
                "Consensus Algorithms: Paxos, Raft & Byzantine Fault Tolerance",
                "Leader-Follower, Multi-Leader & Leaderless (Dynamo) Storage",
                "Consistent Hashing, Sharding & Hot-Key Mitigation",
                "Idempotency Keys, Outbox Pattern & Saga Workflows",
                "Backpressure, Circuit Breakers, Jitter & Load Shedding",
                "Distributed Tracing, Tail Latency (P99) & Error Budgets"
            ),
            synopsis = "Principal-engineer field guide to distributed consensus (Raft/Paxos), CAP/PACELC tradeoffs, consistent hashing, idempotency, backpressure, and P99 reliability.",
            dedication = "For principal engineers designing systems that endure storms.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. Time, Causality & CAP / PACELC Theorems",
                    "NTP clocks drift milliseconds apart and pause during VM migrations or GC cycles, making wall-clock timestamps unsafe for ordering concurrent writes. Lamport clocks and Vector Clocks track causal happens-before (a -> b) relationships directly. Under network partitions (CAP), systems trade linearizability for availability; even without partitions (PACELC), systems trade consistency latency against local speed.",
                    "PACELC: If Partition -> Availability vs Consistency; Else -> Latency vs Consistency.",
                    "Where could trusting client wall-clock timestamps corrupt financial or inventory logs?"
                ),
                BookPage(
                    2,
                    "§2. Consensus Protocols: Raft & Replicated Logs",
                    "Distributed coordination (etcd, ZooKeeper, Spanner) relies on quorum consensus where a majority (⌊N/2⌋ + 1) agrees on a replicated append-only log. Raft splits consensus cleanly into randomized Leader Election, Log Replication across terms, and Safety invariants ensuring committed entries survive leader failover.",
                    "Quorum Math: 3 nodes tolerate 1 failure | 5 nodes tolerate 2 failures.",
                    "Why must a quorum-based write overlap with any subsequent read quorum?"
                ),
                BookPage(
                    3,
                    "§3. Sharding, Idempotency & Saga Patterns",
                    "Horizontal scaling partitions keys across shards via range partitioning or Consistent Hashing with virtual nodes so cluster resizes move only ~1/N of keys. Because networks deliver at-least-once, mutating APIs enforce Idempotency Keys, transactional Outbox tables, and compensating Saga steps instead of brittle two-phase locks across microservices.",
                    "Reliability Rule: At-least-once delivery + Idempotent receiver = Effectively-once state.",
                    "Sketch an idempotency key flow for a book checkout or payment endpoint."
                ),
                BookPage(
                    4,
                    "§4. Backpressure, Tail Latency & SRE Observability",
                    "Retries without exponential backoff and random jitter amplify outages into retry storms. Protect services with bounded queues, adaptive concurrency limits, circuit breakers, and graceful load shedding. Measure P95/P99 tail latency alongside distributed OpenTelemetry traces and Service Level Objectives (SLOs) with explicit Error Budgets.",
                    "Tail Amplification: Calling 10 fan-out services at 99% P99 gives ~9.6% chance of hitting a slow response.",
                    "Search live Google architecture articles for 'Tail at Scale' hedging techniques."
                )
            )
        )
    )
}
