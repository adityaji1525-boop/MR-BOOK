package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.Book
import com.example.model.BookCategory
import com.example.model.BookPage
import com.example.model.EducationLevel

internal object LibraryCatalogPart3 {
    val books: List<Book> = listOf(
        // ================= MATHEMATICS (Shelf 9) =================
        Book(
            id = "math_school",
            title = "Secrets of Patterns & Shapes",
            author = "Ms. Priya Nair",
            category = BookCategory.MATHEMATICS,
            level = EducationLevel.SCHOOL,
            shelfNumber = 9,
            spineColor = Color(0xFF375A7F),
            coverTrimColor = Color(0xFFF2C86D),
            realSubjectScope = "Elementary & Middle-School Mathematics: Number Patterns, Fibonacci & Golden Ratio, Polygon Geometry, Symmetry, Fractions & Area/Volume",
            completeTopics = listOf(
                "Number Theory: Primes, Factors, Multiples & Divisibility",
                "Fractions, Decimals, Ratios & Proportional Thinking",
                "Fibonacci Sequence & Golden Ratio (φ ≈ 1.618) in Nature",
                "Polygons, Regular Tessellations & Honeycomb Hexagons",
                "Symmetry: Reflection, Rotation & Translation",
                "Perimeter, Area, Surface Area & Volume Formulas",
                "Logical Puzzles & Stepwise Word-Problem Craft"
            ),
            synopsis = "Complete foundational mathematics covering primes, ratios, Fibonacci spirals, regular polygon tessellations, symmetry, measurement geometry, and puzzle craft.",
            dedication = "Math is nature's favorite sketchbook.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Primes, Ratios & Proportional Patterns",
                    "Whole numbers break down uniquely into prime building blocks (Fundamental Theorem of Arithmetic: e.g., 60 = 2² × 3 × 5). Ratios and fractions compare parts cleanly (3/4 = 0.75 = 75%), powering everything from baking recipes and architectural scale models to map distances and fair sharing.",
                    "Key Fact: 2 is the only even prime number; 1 is neither prime nor composite.",
                    "Factor today's calendar day or your favorite 2-digit number into primes!"
                ),
                BookPage(
                    2,
                    "2. Fibonacci Numbers & Phyllotaxis Spirals",
                    "Starting with 1, 1, each Fibonacci term adds the previous two: 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89... Dividing consecutive numbers approaches the Golden Ratio φ = (1 + √5)/2 ≈ 1.61803. Pinecones, sunflowers, and succulents pack seeds along golden divergence angles (~137.5°) so new petals never shadow older ones directly.",
                    "Golden Angle: 360° × (1 - 1/φ) ≈ 137.508°.",
                    "Count opposing clockwise and counterclockwise spirals on a pinecone or pineapple!"
                ),
                BookPage(
                    3,
                    "3. Honeycomb Hexagons & Regular Tessellations",
                    "Which regular polygons tile a flat plane without gaps? Interior angles must divide 360° evenly! Only equilateral triangles (60° × 6), squares (90° × 4), and regular hexagons (120° × 3) tessellate cleanly alone. As proved mathematically by the Honeycomb Conjecture (Thomas Hales, 1999), regular hexagons enclose maximum area with minimum perimeter wall material!",
                    "Angle Formula: Interior angle of regular n-gon = 180° × (n - 2) / n.",
                    "Test why regular pentagons (108°) leave gaps when placed corner-to-corner."
                ),
                BookPage(
                    4,
                    "4. Symmetry, Perimeter, Area & 3D Volume",
                    "Shapes stay congruent under rigid transformations: translations (slides), reflections (flips), and rotations (turns). Measure 2D boundaries with Perimeter and interior space with Area (Triangle = ½bh; Circle = πr²; Circumference = 2πr), and 3D capacity with Volume (Prism = Base×h; Cylinder = πr²h; Sphere = 4/3 πr³).",
                    "Circle Constant: π ≈ 3.14159265... (ratio of circumference to diameter).",
                    "Explore live Google Math facts on Archimedes' discovery of sphere-to-cylinder volume!"
                )
            )
        ),
        Book(
            id = "math_high",
            title = "Calculus: Motion & Area",
            author = "Dr. David Kim",
            category = BookCategory.MATHEMATICS,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 9,
            spineColor = Color(0xFF284666),
            coverTrimColor = Color(0xFFE3BA64),
            realSubjectScope = "Single-Variable Calculus (AP Calc AB/BC): Limits, Continuity, Derivatives, Optimization, Integration, Fundamental Theorem & Series",
            completeTopics = listOf(
                "Limits, One-Sided Limits, Continuity & IVT",
                "Definition of the Derivative & Tangent Slopes",
                "Differentiation Rules: Power, Product, Quotient & Chain",
                "Optimization, Related Rates & Mean Value Theorem",
                "Riemann Sums & Definite Integrals (Area Under Curves)",
                "Fundamental Theorem of Calculus (Parts 1 & 2)",
                "U-Substitution, Integration by Parts & Separable ODEs",
                "Infinite Series & Taylor Polynomial Approximations"
            ),
            synopsis = "Complete single-variable calculus textbook covering epsilon-delta intuition, limits, differentiation rules, optimization, Riemann integrals, FTC, ODEs, and Taylor series.",
            dedication = "Zooming in reveals straight lines; zooming out reveals curves.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Limits, Continuity & Instantaneous Rates",
                    "A limit describes what f(x) approaches as x draws arbitrarily close to c—without needing f(c) itself to be defined. Continuity at c requires lim(x->c) f(x) = f(c). Shrinking a secant interval h -> 0 defines the derivative f'(x) = lim(h->0) [f(x+h) - f(x)] / h, turning average speed into exact instantaneous velocity or tangent slope.",
                    "Mean Value Theorem: Some c in (a,b) achieves f'(c) = [f(b) - f(a)] / (b - a).",
                    "Why is sin(x)/x -> 1 as x -> 0 (measured in radians) so foundational?"
                ),
                BookPage(
                    2,
                    "2. Differentiation Rules & Real Optimization",
                    "Core rules compute slopes rapidly: Power Rule d/dx(xⁿ) = nxⁿ⁻¹, Product Rule (fg)' = f'g + fg', Quotient Rule, and the indispensable Chain Rule d/dx f(g(x)) = f'(g(x))·g'(x). Setting f'(x) = 0 finds critical maxima and minima tested via f''(x) concavity—optimizing packaging volume, travel routes, and marginal economic profit.",
                    "Common Derivatives: d/dx(eˣ) = eˣ | d/dx(ln x) = 1/x | d/dx(sin x) = cos x.",
                    "Find two positive numbers summing to 20 whose product is as large as possible!"
                ),
                BookPage(
                    3,
                    "3. Riemann Sums & The Fundamental Theorem",
                    "Integrating continuous non-negative f(x) over [a, b] sums N rectangles of width Δx = (b-a)/N as N -> ∞: ∫ₐᵇ f(x) dx = lim Σ f(xᵢ*)Δx. Newton and Leibniz's crowning insight—the Fundamental Theorem of Calculus—unites slope and area as exact inverses: d/dx ∫ₐˣ f(t)dt = f(x), and ∫ₐᵇ f(x)dx = F(b) - F(a) for any antiderivative F.",
                    "Fundamental Theorem: ∫ₐᵇ F'(x) dx = F(b) - F(a).",
                    "Verify geometrically why ∫₀⁴ (2x) dx equals 4² = 16!"
                ),
                BookPage(
                    4,
                    "4. Differential Equations & Taylor Series",
                    "Laws of growth, radioactive decay, and cooling obey separable differential equations such as dy/dt = ky (solution y = y₀e^{kt}) or logistic growth dy/dt = ry(1 - y/K). Smooth functions expand locally into infinite Taylor Series Σ [f⁽ⁿ⁾(a)/n!] (x-a)ⁿ—allowing calculators and spacecraft computers to evaluate sin(x), cos(x), and eˣ using pure arithmetic!",
                    "Maclaurin Expansions: eˣ = 1 + x + x²/2! + x³/3! + ... | cos x = 1 - x²/2! + x⁴/4! - ...",
                    "Explore live Google visual calculus resources on Taylor polynomial convergence!"
                )
            )
        ),
        Book(
            id = "math_uni",
            title = "Linear Algebra & Vector Spaces",
            author = "Prof. Sofia Almeida",
            category = BookCategory.MATHEMATICS,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 9,
            spineColor = Color(0xFF1F3854),
            coverTrimColor = Color(0xFFEBC36B),
            realSubjectScope = "University Linear Algebra: Vector Spaces, Linear Transformations, Fundamental Subspaces, Eigenvalues/Eigenvectors, Orthogonality & SVD",
            completeTopics = listOf(
                "Vector Spaces, Span, Linear Independence & Basis",
                "Gaussian Elimination, RREF & Rank-Nullity Theorem",
                "Linear Transformations & Matrix Multiplication Geometry",
                "Determinants, Invertibility & Volume Scaling",
                "Four Fundamental Subspaces (Column, Row, Null, Left Null)",
                "Eigenvalues, Eigenvectors, Trace & Diagonalization (A = PDP⁻¹)",
                "Orthogonal Projections, Gram-Schmidt & Least Squares",
                "Symmetric Matrices, Spectral Theorem & SVD (A = UΣVᵀ)"
            ),
            synopsis = "Complete university linear algebra reference connecting vector geometry, Rank-Nullity, four subspaces, eigenvalues, Gram-Schmidt least squares, and SVD.",
            dedication = "Matrices are verbs acting upon space.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Vector Spaces, Basis & Linear Transformations",
                    "A vector space over ℝ or ℂ supports linear combinations c₁v₁ + ... + cₖvₖ. A basis is a linearly independent spanning set whose size uniquely defines the space's dimension. Every linear map T(u + v) = T(u) + T(v), T(cu) = cT(u) is encoded cleanly by a matrix A whose columns specify exactly where the standard basis vectors e₁, ..., eₙ land.",
                    "Rank-Nullity Theorem: rank(A) + nullity(A) = number of columns (n).",
                    "Visualize how a 2×2 rotation matrix [cos θ, -sin θ; sin θ, cos θ] moves unit basis vectors."
                ),
                BookPage(
                    2,
                    "2. Determinants & The Four Fundamental Subspaces",
                    "The determinant det(A) measures the signed n-dimensional volume scaling factor of transformation A; A is invertible iff det(A) ≠ 0 and rank(A) = n. Gilbert Strang's Four Fundamental Subspaces—Column Space C(A) ⟂ Left Nullspace N(Aᵀ), and Row Space C(Aᵀ) ⟂ Nullspace N(A)—describe the complete geometry of Ax = b.",
                    "Invertibility Equivalence: det(A) ≠ 0 <=> Ax = 0 has only x = 0 <=> full rank.",
                    "Why does orthogonal complement structure guarantee Ax = b either solves cleanly or projects?"
                ),
                BookPage(
                    3,
                    "3. Eigenvalues, Eigenvectors & Diagonalization",
                    "Nonzero eigenvectors satisfy Av = λv: transformations stretch them along invariant directions scaled by eigenvalue λ, found via characteristic polynomial det(A - λI) = 0. When A possesses n independent eigenvectors, A = P D P⁻¹ makes computing high powers Aᵏ = P Dᵏ P⁻¹ instantaneous—powering Google PageRank, Markov chains, and vibration modes.",
                    "Invariants: sum(λᵢ) = trace(A) | product(λᵢ) = det(A).",
                    "Look up the Spectral Theorem guaranteeing real orthogonal eigenvectors for symmetric matrices!"
                ),
                BookPage(
                    4,
                    "4. Least Squares & Singular Value Decomposition",
                    "Overdetermined systems Ax ≈ b with no exact solution minimize error ‖Ax - b‖² via orthogonal projection onto C(A), solving Normal Equations AᵀAx̂ = Aᵀb (or stable QR factorization via Gram-Schmidt). Universally, ANY rectangular m×n matrix factors via SVD into A = U Σ Vᵀ (orthogonal rotation, axis scaling, orthogonal rotation)—driving PCA, image compression, and low-rank matrix approximations.",
                    "Factorizations: LU (elimination) | QR (orthogonal) | SVD A = UΣVᵀ (universal).",
                    "Use live Google Search to see how SVD compresses images and powers recommender systems."
                )
            )
        ),
        Book(
            id = "math_pro",
            title = "Topology, Proofs & Optimization",
            author = "Dr. Alan Mercer",
            category = BookCategory.MATHEMATICS,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 9,
            spineColor = Color(0xFF15273D),
            coverTrimColor = Color(0xFFF3CC73),
            realSubjectScope = "Graduate Algebraic Topology & Convex Optimization: Point-Set Topology, Homotopy, Euler Characteristic, Convex Duality, KKT Conditions & Interior-Point Solvers",
            completeTopics = listOf(
                "Topological Spaces, Open Sets, Compactness & Connectedness",
                "Homeomorphisms, Quotient Spaces & Surfaces Classification",
                "Euler Characteristic χ = V - E + F = 2 - 2g & Genus",
                "Fundamental Group π₁(X), Homotopy & Singular Homology",
                "Convex Sets, Convex Functions & Epigraph Geometry",
                "Lagrange Duality, Slater's Condition & Strong Duality",
                "Karush-Kuhn-Tucker (KKT) Optimality Conditions",
                "Gradient, Subgradient, Proximal & Interior-Point Methods"
            ),
            synopsis = "Graduate mathematical synthesis uniting point-set & algebraic topology (homeomorphisms, genus, π₁, homology) with convex duality, KKT theory, and modern solvers.",
            dedication = "Rigorous proof is music written in certainty.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. Topological Spaces, Compactness & Genus",
                    "A topology τ on set X specifies open subsets closed under arbitrary unions and finite intersections—capturing continuity (preimages of open sets remain open) without requiring metric distances. Compactness (every open cover admits a finite subcover) generalizes closed-bounded sets via Heine-Borel. Closed orientable surfaces are classified up to homeomorphism purely by genus g (holes) via Euler Characteristic χ = V - E + F = 2 - 2g.",
                    "Surface Invariants: Sphere (g=0, χ=2) | Torus (g=1, χ=0) | Two-hole Torus (g=2, χ=-2).",
                    "Verify V - E + F = 2 across a cube, tetrahedron, and icosahedron!"
                ),
                BookPage(
                    2,
                    "§2. Fundamental Group & Algebraic Homology",
                    "To prove two spaces are NOT homeomorphic, algebraic topology attaches algebraic invariants: the Fundamental Group π₁(X, x₀) tracks homotopy classes of loops (trivial for S²; ℤ for S¹; ℤ × ℤ for the torus T²), while singular homology Hₙ(X) detects n-dimensional voids systematically and powers Topological Data Analysis (persistent homology).",
                    "Brouwer Fixed Point Proof: π₁(D²) = 0 while π₁(S¹) = ℤ forbids continuous retraction!",
                    "Search live mathematics literature on Perelman's proof of the Poincaré Conjecture."
                ),
                BookPage(
                    3,
                    "§3. Convexity, Lagrange Duality & KKT Conditions",
                    "A function f is convex if chords lie on or above its graph: f(θx + (1-θ)y) ≤ θf(x) + (1-θ)f(y), guaranteeing every local minimum is globally optimal. Dualizing constraints via Lagrangian L(x, λ, ν) yields a concave dual problem g(λ, ν) ≤ p*; under Slater's strictly feasible condition, strong duality holds (d* = p*) and optimal primal-dual pairs satisfy KKT stationarity, primal/dual feasibility, and complementary slackness (λᵢ fᵢ(x*) = 0).",
                    "KKT Complementary Slackness: Inactive constraint (fᵢ < 0) => Dual multiplier λᵢ = 0.",
                    "Why are SVMs, LASSO regression, and portfolio optimization naturally convex?"
                ),
                BookPage(
                    4,
                    "§4. First-Order, Proximal & Interior-Point Solvers",
                    "Large-scale optimization balances iteration speed vs precision: Nesterov accelerated gradient achieves O(1/k²) smooth convergence; subgradient & proximal splitting (ADMM, ISTA) solve non-smooth ℓ₁ sparsity problems; Primal-Dual Interior-Point methods replace constraints with log-barriers and Newton steps to reach ε-accuracy in polynomial O(√m log(1/ε)) outer steps.",
                    "Method Match: Massive data -> Accelerated/Proximal First-Order | High precision -> Interior-Point.",
                    "Check recent live optimization advances in ML training (e.g., Muon/Shampoo/AdamW variants)."
                )
            )
        ),

        // ================= HISTORY & PHILOSOPHY (Shelf 10) =================
        Book(
            id = "his_school",
            title = "Builders of Ancient Libraries",
            author = "Noor Al-Mansoor",
            category = BookCategory.HISTORY_PHILOSOPHY,
            level = EducationLevel.SCHOOL,
            shelfNumber = 10,
            spineColor = Color(0xFF5A6E36),
            coverTrimColor = Color(0xFFF0C973),
            realSubjectScope = "World Ancient History & Civilization Foundations: Mesopotamia, Egypt, Indus Valley, Qin/Han China, Alexandria & Medieval Houses of Wisdom",
            completeTopics = listOf(
                "Mesopotamia: Cuneiform Clay Tablets & Library of Ashurbanipal",
                "Ancient Egypt: Nile Papyrus, Hieroglyphs & Scroll Craft",
                "The Royal Library of Alexandria & Eratosthenes",
                "Indus Valley, Nalanda & Ancient South Asian Learning",
                "Ancient China: Oracle Bones, Bamboo Slips & Cai Lun's Paper",
                "Baghdad's House of Wisdom (Bayt al-Hikma) & Al-Khwarizmi",
                "Preserving Historical Manuscripts & Timbuktu Archives"
            ),
            synopsis = "Complete school world-history journey from Sumerian clay tablets and Egyptian papyrus to Alexandria, Cai Lun's paper, Nalanda, and Baghdad's House of Wisdom.",
            dedication = "Every library remembers voices across centuries.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Sumerian Clay Tablets & Nineveh's Library",
                    "Around 3200 BCE in Mesopotamia (modern Iraq), scribes pressed reed styluses into damp river clay to write cuneiform wedge scripts. Centuries later, Assyrian King Ashurbanipal (7th century BCE) assembled over 30,000 indexed clay tablets in Nineveh—cataloging astronomy, law, dictionaries, and the Epic of Gilgamesh—many preserved by the very fire that baked the clay permanent.",
                    "Historical Fact: Ashurbanipal's colophons listed tablet series number and shelf owner!",
                    "What survival story from your own family deserves to be preserved forever?"
                ),
                BookPage(
                    2,
                    "2. Nile Papyrus & The Library of Alexandria",
                    "Along the Nile River, Egyptians wove criss-crossed papyrus reeds into supple scrolls read across Mediterranean antiquity. Founded under the Ptolemaic rulers in Egypt (~3rd century BCE), the Library of Alexandria aimed to gather all Greek, Egyptian, Babylonian, and Indian knowledge—where head librarian Eratosthenes calculated Earth's circumference within ~2% using noon shadow angles!",
                    "Measurement Milestone: Eratosthenes measured Alexandria vs Syene shadows (~240 BCE).",
                    "Explore live Google History details about Callimachus's Pinakes—the first library catalog!"
                ),
                BookPage(
                    3,
                    "3. Ancient China (Paper), Nalanda & House of Wisdom",
                    "In Han Dynasty China (105 CE), court official Cai Lun refined papermaking from mulberry bark, hemp, and cloth rags—a craft that later traveled West along the Silk Road. Centuries on, India's residential university of Nalanda and 9th-century Baghdad's House of Wisdom (Bayt al-Hikma)—where Al-Khwarizmi authored foundational algebra (al-jabr)—translated and expanded Greek, Sanskrit, and Persian science.",
                    "Global Legacy: The word 'algorithm' honors Baghdad mathematician Al-Khwarizmi!",
                    "Trace how paper traveled from Han China to Samarkand, Baghdad, and Europe."
                ),
                BookPage(
                    4,
                    "4. Timbuktu Manuscripts & Cultural Stewardship",
                    "Across West Africa from the 13th to 16th centuries, Mali's Timbuktu scholar families copied and safeguarded hundreds of thousands of astronomy, poetry, law, and medical manuscripts—passing quiet stewardship across generations so humanity never forgets its shared past.",
                    "Stewardship Lesson: Knowledge belongs to the whole human family when safeguarded generously.",
                    "Write one historical question you want to check using Google Search below."
                )
            )
        ),
        Book(
            id = "his_high",
            title = "Crossroads of World History",
            author = "Dr. Thomas Grant",
            category = BookCategory.HISTORY_PHILOSOPHY,
            level = EducationLevel.HIGH_SCHOOL,
            shelfNumber = 10,
            spineColor = Color(0xFF475929),
            coverTrimColor = Color(0xFFE3BC66),
            realSubjectScope = "High School World History: Afro-Eurasian Silk Road, Printing & Reformation, Columbian Exchange, Scientific & Industrial Revolutions & Modern Era",
            completeTopics = listOf(
                "Silk Road & Indian Ocean Maritime Trade Networks",
                "Woodblock Printing (East Asia) & Gutenberg Movable Type",
                "Renaissance Humanism & The Scientific Revolution",
                "The Columbian Exchange & Early Modern Global Empires",
                "Enlightenment Political Philosophy & Constitutional Revolutions",
                "Industrial Revolution: Steam, Rail, Labor & Reform",
                "20th-Century Decolonization, Cold War & Information Age"
            ),
            synopsis = "Complete high-school world history textbook connecting Silk Road & Indian Ocean trade, Gutenberg printing, Scientific & Industrial Revolutions, and the modern world.",
            dedication = "History is not only dates—it is choices made by people like us.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Silk Road & Indian Ocean Maritime Exchange",
                    "For over 1,500 years, overland Silk Road caravans and monsoon-powered Indian Ocean dhows linked Chang'an, Samarkand, Kilwa, Calicut, Cairo, and Venice. Beyond silk, porcelain, spices, and gold, travelers exchanged Hindu-Arabic numerals, astrolabes, magnetic compasses, medical texts, and world religions—demonstrating history is fundamentally interconnected.",
                    "Turning Point: Maritime monsoon navigation made bulk oceanic trade faster than desert routes.",
                    "Trace three everyday foods or inventions shaped by Afro-Eurasian exchange."
                ),
                BookPage(
                    2,
                    "2. Printing Press, Renaissance & Scientific Revolution",
                    "Building upon East Asian woodblock and Korean/Chinese movable type, Johannes Gutenberg's ~1440 metal movable-type press slashed book costs across Europe, fueling literacy, Renaissance humanism, and scientific publishing. Copernicus, Galileo, Kepler, Ibn al-Haytham's empirical optics legacy, and Newton replaced unchecked authority with repeatable observation and mathematical law.",
                    "Impact: Books in Europe surged from tens of thousands of manuscripts to ~20 million printed volumes by 1500!",
                    "How did rapid print sharing accelerate Galileo's and Newton's discoveries?"
                ),
                BookPage(
                    3,
                    "3. Enlightenment, Revolutions & Industrialization",
                    "Enlightenment thinkers (Locke, Montesquieu, Rousseau, Wollstonecraft) challenged absolute monarchy with natural rights, separation of powers, and civic consent—inspiring constitutional debates across the Americas, France, and Haiti (the first republic founded by formerly enslaved people). Simultaneously, Watt's coal steam engine sparked the Industrial Revolution—transforming agriculture, factories, cities, and labor rights.",
                    "Historical Connections: Industrial energy transformed geopolitics, urbanization, and social reform.",
                    "Compare how industrial technology created both productivity booms and labor movements."
                ),
                BookPage(
                    4,
                    "4. Modern Geopolitics, Decolonization & Digital Era",
                    "After the devastation of two World Wars and the founding of the United Nations (1945) & Universal Declaration of Human Rights (1948), Asia and Africa experienced historic decolonization movements alongside Cold War ideological rivalry. Since 1990, semiconductors, the World Wide Web, and green-energy transitions have woven humanity into an instant planetary library.",
                    "Primary Source Lens: Examine cause, contingency, continuity, and change over time.",
                    "Search live Google history archives on any turning point in the 20th or 21st century!"
                )
            )
        ),
        Book(
            id = "his_uni",
            title = "Ethics & The Examined Life",
            author = "Prof. Clara Lind",
            category = BookCategory.HISTORY_PHILOSOPHY,
            level = EducationLevel.UNIVERSITY,
            shelfNumber = 10,
            spineColor = Color(0xFF394720),
            coverTrimColor = Color(0xFFEBC36B),
            realSubjectScope = "University Moral & Ancient Philosophy: Socratic Elenchus, Aristotelian Virtue Ethics, Stoicism, Kantian Deontology, Utilitarianism, Rawls & AI Ethics",
            completeTopics = listOf(
                "Socratic Method (Elenchus) & Plato's Theory of Justice",
                "Aristotle's Nicomachean Ethics: Virtue & Eudaimonia",
                "Stoic Philosophy: Epictetus, Seneca & Marcus Aurelius",
                "Kantian Deontology & The Categorical Imperative",
                "Consequentialism & Utilitarianism (Bentham, J.S. Mill)",
                "Social Contract & Rawls's Veil of Ignorance",
                "Applied Ethics: Bioethics, Climate & AI Governance"
            ),
            synopsis = "Complete university ethics & philosophy course comparing Socratic inquiry, Aristotle's Golden Mean, Stoicism, Kant, Mill, John Rawls, and contemporary AI & bioethics.",
            dedication = "Wisdom begins in unhurried reflection.",
            pages = listOf(
                BookPage(
                    1,
                    "1. Socrates, Plato & Aristotle's Virtue Ethics",
                    "Socrates declared 'The unexamined life is not worth living,' using elenchus questioning to expose hidden contradictions. Aristotle's Nicomachean Ethics locates flourishing (eudaimonia) in virtuous habit: moral virtues like courage and generosity occupy a rational Golden Mean between excess (rashness) and deficiency (cowardice), guided by practical wisdom (phronesis).",
                    "Aristotle's Principle: Virtue is an educated hexis (habitual disposition), not a passing mood.",
                    "Identify one daily habit that sits at the Golden Mean between two extremes."
                ),
                BookPage(
                    2,
                    "2. The Stoic Citadel & Dichotomy of Control",
                    "Roman Stoics—formerly enslaved philosopher Epictetus, statesman Seneca, and Emperor Marcus Aurelius—centered tranquility on separating what is up to us (eph' hemin: reasoned judgment, intention, fairness) from externals outside full control (reputation, illness, weather, praise). Stoicism trains calm civic duty rather than cold withdrawal.",
                    "Epictetus's Enchiridion #1: 'Some things are in our control and others not.'",
                    "Name one stressor today belonging outside your direct control that you can release."
                ),
                BookPage(
                    3,
                    "3. Kantian Duty vs. Utilitarian Outcomes",
                    "Immanuel Kant grounded morality in rational duty rather than consequences: the Categorical Imperative demands we act only on universalizable maxims and ALWAYS treat humanity as an end in itself, never merely as a tool. Conversely, Jeremy Bentham and John Stuart Mill's Utilitarianism weighs actions by aggregate well-being and minimized suffering.",
                    "Compare Frameworks: Deontology (Duty/Rights) vs Consequentialism (Outcomes/Utility).",
                    "Test a classic Trolley or privacy dilemma under both Kantian and Millian rules!"
                ),
                BookPage(
                    4,
                    "4. Rawls's Veil of Ignorance & Modern AI Ethics",
                    "John Rawls (A Theory of Justice) asked how fair rules look behind a 'Veil of Ignorance' where you don't know your wealth, health, or status at birth—justifying equal basic liberties and the Difference Principle protecting the most vulnerable. Today, those exact frameworks guide algorithmic fairness, consent, climate stewardship, and AI alignment.",
                    "Modern Challenge: Ensuring transparent accountability, privacy, and human dignity in software.",
                    "Use live Google Search below to examine current UNESCO or IEEE AI Ethics guidelines!"
                )
            )
        ),
        Book(
            id = "his_pro",
            title = "Epistemology & Leadership Wisdom",
            author = "Dr. Samuel Osei",
            category = BookCategory.HISTORY_PHILOSOPHY,
            level = EducationLevel.PROFESSIONAL,
            shelfNumber = 10,
            spineColor = Color(0xFF283316),
            coverTrimColor = Color(0xFFF4CE78),
            realSubjectScope = "Executive Epistemology, Judgment Under Uncertainty, Bayesian Updating, Systems Thinking, Governance & Institutional Stewardship",
            completeTopics = listOf(
                "Applied Epistemology & Intellectual Humility",
                "Bayesian Belief Updating & Calibration Against Forecasts",
                "First-Order vs Second-Order Systems Thinking",
                "Pre-Mortems, Red-Teaming & Psychological Safety",
                "Principal-Agent Alignment & Governance Integrity",
                "Cathedral Thinking & Multi-Generational Stewardship",
                "Mentorship, Succession & Institutional Memory"
            ),
            synopsis = "Executive & scholarly manual covering Bayesian belief calibration, second-order systems dynamics, pre-mortem risk design, governance ethics, and cathedral stewardship.",
            dedication = "Stewardship leaves the library richer than we found it.",
            pages = listOf(
                BookPage(
                    1,
                    "§1. Applied Epistemology & Bayesian Calibration",
                    "Leadership collapses when ego fuses with old hypotheses. Intellectual humility treats beliefs as living probability estimates updated continuously via Bayes' Theorem as fresh empirical evidence arrives—distinguishing high-confidence invariants from reversible experiments while inviting rigorous, good-faith dissent before irreversible commitments.",
                    "Calibration Habit: Keep decision logs recording assumptions, confidence %, and review dates!",
                    "Write down one professional assumption you updated cleanly when facts shifted."
                ),
                BookPage(
                    2,
                    "§2. Systems Thinking, Second-Order Effects & Pre-Mortems",
                    "Naive interventions optimize local metrics while triggering devastating second-order feedback loops elsewhere (Goodhart's Law: when a measure becomes a target, it ceases to be a good measure). Before launch, run Gary Klein's Pre-Mortem: assume the initiative failed cleanly a year from now, and trace every hidden fragility proactively.",
                    "Tool: Causal Loop Diagrams + Pre-Mortem Dissent + Psychological Safety.",
                    "Spot one metric in your field vulnerable to Goodhart's Law distortion."
                ),
                BookPage(
                    3,
                    "§3. Ethical Governance & Cathedral Stewardship",
                    "Short-horizon incentives erode trust quietly; enduring institutions practice 'Cathedral Thinking'—building transparent governance, clear accountability, and open documentation for stakeholders and colleagues decades into the future. True leadership measures legacy not by personal spotlight, but by the strength and integrity of the builders it mentors.",
                    "Stewardship Standard: Leave every team, codebase, and library clearer than you inherited it.",
                    "Search live Google leadership case studies on long-term institutional governance."
                )
            )
        )
    )
}
