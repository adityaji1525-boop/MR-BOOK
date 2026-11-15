package com.example.model

import androidx.compose.ui.graphics.Color
import org.json.JSONArray
import org.json.JSONObject

enum class CustomBaseChassis(val id: String, val label: String) {
    BLANK_WORKBENCH("blank", "BLANK WORKBENCH"),
    ORBITAL_RIG("orbit", "ORBIT & RINGS"),
    MOTOR_CIRCUIT("motor", "CIRCUIT & COIL"),
    REACTION_CONE("cone", "CONE CHAMBER"),
    TURBINE_TOWER("tower", "TOWER & ROTOR"),
    HYDRAULIC_PISTON("piston", "PISTON LIFT"),
    OPTICS_TUBE("optics", "OPTICS BEAM"),
    FILTER_COLUMN("column", "FLUID COLUMN")
}

enum class ScienceModelTemplate(
    val id: String,
    val title: String,
    val categoryTag: String,
    val accentColor: Color,
    val chassis: CustomBaseChassis,
    val materials: List<String>,
    val buildSteps: List<String>,
    val scientificPrinciple: String,
    val baseTextEffects: List<String>,
    val defaultAngleDeg: Int,
    val angleLabel: String,
    val driveLabel: String
) {
    SOLAR_SYSTEM(
        id = "solar_system",
        title = "SOLAR SYSTEM ORRERY",
        categoryTag = "ASTRONOMY • MECHANICS",
        accentColor = Color(0xFFD49B35),
        chassis = CustomBaseChassis.ORBITAL_RIG,
        materials = listOf(
            "CENTRAL GLOWING SUN SPHERE (LED CORE)",
            "4 COLORED PLANET SPHERES (MERCURY, VENUS, EARTH, MARS)",
            "CONCENTRIC BRASS ORBIT RINGS & AXLE",
            "BATTERY PACK & GEAR DRIVE BASE"
        ),
        buildSteps = listOf(
            "1. MOUNT THE CENTRAL SUN LED SPHERE ON THE MAIN BRASS SPINDLE.",
            "2. ATTACH EACH PLANET ARM TO SEPARATE CONCENTRIC ORBIT RINGS.",
            "3. SET ORBITAL SPACING SO INNER AND OUTER PLANETS NEVER COLLIDE.",
            "4. CONNECT THE BASE MOTOR GEAR AND SWITCH ON POWER."
        ),
        scientificPrinciple = "KEPLER'S LAWS & GRAVITATIONAL ORBITS: INNER PLANETS COMPLETE REVOLUTIONS FASTER THAN OUTER PLANETS AROUND THE CENTRAL MASS.",
        baseTextEffects = listOf(
            "LIGHT EFFECT: CENTRAL SUN GLOW CASTS RADIANT ILLUMINATION ACROSS INNER HEMISPHERES OF EACH PLANET.",
            "MOTION EFFECT: CONCENTRIC RINGS REVOLVE AT PROPORTIONAL ANGULAR VELOCITIES.",
            "EDUCATIONAL EFFECT: CLEARLY DEMONSTRATES DAY-SIDE VS NIGHT-SIDE AND ORBITAL PERIOD RATIOS."
        ),
        defaultAngleDeg = 23,
        angleLabel = "AXIS TILT",
        driveLabel = "ORBIT SPEED"
    ),
    ELECTRIC_MOTOR(
        id = "electric_motor",
        title = "DC ELECTRIC MOTOR & CIRCUIT",
        categoryTag = "PHYSICS • ELECTROMAGNETISM",
        accentColor = Color(0xFF28588A),
        chassis = CustomBaseChassis.MOTOR_CIRCUIT,
        materials = listOf(
            "ENAMELED COPPER ARMATURE COIL",
            "TWO PERMANENT MAGNET POLES (NORTH & SOUTH)",
            "COMMUTATOR RINGS, BRUSHES & 9V BATTERY",
            "WOODEN BASEBOARD & INSULATED WIRES"
        ),
        buildSteps = listOf(
            "1. WIND 20 TURNS OF COPPER WIRE INTO A BALANCED RECTANGULAR ROTOR COIL.",
            "2. MOUNT THE COIL AXLE ON CONDUCTING CRADLE SUPPORTS.",
            "3. PLACE NORTH AND SOUTH MAGNETS ON OPPOSITE SIDES WITH CLEARANCE.",
            "4. WIRE THE BATTERY THROUGH A PROTECTIVE SWITCH AND RESISTOR."
        ),
        scientificPrinciple = "LORENTZ FORCE (F = I × L × B): CURRENT PASSING THROUGH THE COIL IN A MAGNETIC FIELD PRODUCES OPPOSITE FORCES ON EACH SIDE, CREATING TORQUE.",
        baseTextEffects = listOf(
            "ELECTROMAGNETIC EFFECT: COIL BECOMES AN ELECTROMAGNET AND EXPERIENCES CONTINUOUS ROTATIONAL TORQUE.",
            "ENERGY CONVERSION: ELECTRICAL ENERGY FROM BATTERY TRANSFORMS INTO MECHANICAL SPIN.",
            "SPARK CHECK: CLEAN COMMUTATOR CONTACT KEEPS ROTATION SMOOTH WITHOUT OVERHEATING."
        ),
        defaultAngleDeg = 45,
        angleLabel = "BRUSH ANGLE",
        driveLabel = "VOLTAGE DRIVE"
    ),
    VOLCANO_MODEL(
        id = "volcano_model",
        title = "VOLCANO ERUPTION MODEL",
        categoryTag = "EARTH SCIENCE • CHEMISTRY",
        accentColor = Color(0xFFA83226),
        chassis = CustomBaseChassis.REACTION_CONE,
        materials = listOf(
            "TAPERED VOLCANO CONE & CENTRAL MAGMA CHAMBER BOTTLE",
            "BAKING SODA (SODIUM BICARBONATE) BASE CHARGE",
            "VINEGAR (ACETIC ACID) + NATURAL RED FOOD COLOR",
            "SEALED CATCHMENT TRAY FOR LAVA FLOW"
        ),
        buildSteps = listOf(
            "1. BUILD A STURDY CONE AROUND THE CENTRAL REACTION CHAMBER.",
            "2. SEAL THE CHAMBER BASE SO PRESSURE FORCES FOAM UPWARD ONLY.",
            "3. POUR BAKING SODA AND DISH SOAP INTO THE INNER CRATER.",
            "4. ADD ACID REACTANT SOLUTION TO TRIGGER CARBON DIOXIDE ERUPTION."
        ),
        scientificPrinciple = "ACID-BASE REACTION: NAHCO3 + CH3COOH RELEASES CARBON DIOXIDE GAS (CO2), BUILDING PRESSURE THAT EXPELS COLORED FOAM LIKE MAGMA.",
        baseTextEffects = listOf(
            "CHEMICAL EFFECT: RAPID CO2 GAS BUBBLES EXPAND INSIDE THE VENT CHAMBER.",
            "VISUAL EFFECT: BRIGHT FOAMING LAVA SURGES OVER THE CRATER RIM AND FLOWS DOWN CHANNELS.",
            "PRESSURE EFFECT: PROPER BOTTOM SEAL MAXIMIZES UPWARD PLUME HEIGHT."
        ),
        defaultAngleDeg = 60,
        angleLabel = "CONE SLOPE",
        driveLabel = "REACTANT DOSE"
    ),
    WIND_TURBINE(
        id = "wind_turbine",
        title = "WIND TURBINE GENERATOR",
        categoryTag = "RENEWABLE ENERGY • AERO",
        accentColor = Color(0xFF1E6B52),
        chassis = CustomBaseChassis.TURBINE_TOWER,
        materials = listOf(
            "3 AERODYNAMIC ROTOR BLADES & CENTRAL HUB",
            "NACELLE GENERATOR HOUSING & SUPPORT TOWER",
            "LED BULB TEST LOAD & CONNECTING LEADS",
            "WEIGHTED BASE PLATE FOR STABILITY"
        ),
        buildSteps = listOf(
            "1. ANCHOR THE VERTICAL TOWER SECURELY ONTO THE WEIGHTED BASE.",
            "2. MOUNT THE GENERATOR NACELLE AT THE TOP OF THE TOWER.",
            "3. SET ALL 3 ROTOR BLADES TO AN AERODYNAMIC 25°–40° PITCH ANGLE.",
            "4. WIRE GENERATOR TERMINALS TO THE LED OUTPUT INDICATOR."
        ),
        scientificPrinciple = "FARADAY'S LAW & LIFT AERODYNAMICS: ANGLED BLADES CONVERT WIND KINETIC ENERGY INTO TORQUE, SPINNING COILS PAST MAGNETS TO INDUCE VOLTAGE.",
        baseTextEffects = listOf(
            "AERODYNAMIC EFFECT: AIRFLOW PUSHES ANGLED BLADES INTO STEADY CLOCKWISE ROTATION.",
            "ELECTRICAL EFFECT: INDUCED CURRENT LIGHTS THE TEST LED PROPORTIONAL TO WIND SPEED.",
            "STABILITY EFFECT: BALANCED 3-BLADE HUB ELIMINATES TOWER VIBRATION."
        ),
        defaultAngleDeg = 32,
        angleLabel = "BLADE PITCH",
        driveLabel = "WIND SPEED"
    ),
    HYDRAULIC_LIFT(
        id = "hydraulic_lift",
        title = "HYDRAULIC BRIDGE LIFT",
        categoryTag = "FLUID MECHANICS • ENGINEERING",
        accentColor = Color(0xFF5D3A6A),
        chassis = CustomBaseChassis.HYDRAULIC_PISTON,
        materials = listOf(
            "TWO SEALED SYRINGE PISTONS (INPUT & LIFT CYLINDER)",
            "WATER-FILLED FLEXIBLE SILICONE TUBING",
            "HINGED BRIDGE DECK & SUPPORT PIERS",
            "TIGHT CLAMP COLLARS ON BOTH NOZZLES"
        ),
        buildSteps = listOf(
            "1. FILL THE HYDRAULIC LINE COMPLETELY WITH WATER, REMOVING AIR BUBBLES.",
            "2. CLAMP TUBING FIRMLY ONTO BOTH PISTON NOZZLES TO PREVENT LEAKS.",
            "3. MOUNT THE LIFT CYLINDER DIRECTLY UNDER THE HINGED BRIDGE DECK.",
            "4. PRESS THE INPUT PISTON TO RAISE THE BRIDGE SMOOTHLY."
        ),
        scientificPrinciple = "PASCAL'S PRINCIPLE: PRESSURE APPLIED TO AN ENCLOSED INCOMPRESSIBLE FLUID TRANSMITS EQUALLY IN ALL DIRECTIONS, MULTIPLYING LIFT FORCE.",
        baseTextEffects = listOf(
            "PRESSURE EFFECT: PUSHING THE SMALL INPUT PISTON TRANSFERS FORCE INSTANTLY TO THE LIFT PISTON.",
            "MECHANICAL EFFECT: HINGED BRIDGE DECK RISES SMOOTHLY TO ALLOW PASSAGE BELOW.",
            "FLUID EFFECT: AIR-FREE WATER COLUMN PREVENTS SPONGY LAG."
        ),
        defaultAngleDeg = 45,
        angleLabel = "MAX LIFT",
        driveLabel = "PISTON FORCE"
    ),
    OPTICAL_PERISCOPE(
        id = "optical_periscope",
        title = "45° MIRROR PERISCOPE",
        categoryTag = "OPTICS • LIGHT REFLECTION",
        accentColor = Color(0xFF2B6F7A),
        chassis = CustomBaseChassis.OPTICS_TUBE,
        materials = listOf(
            "RIGID VERTICAL VIEWING TUBE WITH TOP & BOTTOM WINDOWS",
            "TWO FLAT FRONT-SURFACE MIRRORS",
            "TWO 45° MOUNTING BRACKETS",
            "MATTE BLACK INTERIOR BAFFLE TO REDUCE GLARE"
        ),
        buildSteps = listOf(
            "1. CUT PARALLEL VIEWING OPENINGS AT OPPOSITE ENDS OF THE TUBE.",
            "2. MOUNT THE TOP MIRROR AT EXACTLY 45° FACING THE TOP WINDOW.",
            "3. MOUNT THE BOTTOM MIRROR PARALLEL AT 45° FACING THE EYEPIECE.",
            "4. TEST WITH A HORIZONTAL LIGHT RAY TO VERIFY SHARP ALIGNMENT."
        ),
        scientificPrinciple = "LAW OF REFLECTION (ANGLE OF INCIDENCE = ANGLE OF REFLECTION): TWO PARALLEL 45° MIRRORS DEFLECT LIGHT 90° TWICE FOR CLEAR OVER-OBSTACLE VISION.",
        baseTextEffects = listOf(
            "OPTICAL EFFECT: INCOMING HORIZONTAL RAYS BOUNCE 90° DOWN THE SHAFT AND 90° OUT TO THE EYE.",
            "IMAGE EFFECT: OBJECTS ABOVE BARRIERS APPEAR UPRIGHT AND UNDISTORTED.",
            "CONTRAST EFFECT: DARK TUBE INTERIOR BLOCKS STRAY SIDE REFLECTIONS."
        ),
        defaultAngleDeg = 45,
        angleLabel = "MIRROR ANGLE",
        driveLabel = "LIGHT BEAM"
    ),
    WATER_FILTRATION(
        id = "water_filtration",
        title = "MULTI-LAYER WATER FILTER",
        categoryTag = "ENVIRONMENTAL SCIENCE",
        accentColor = Color(0xFF2E7D6B),
        chassis = CustomBaseChassis.FILTER_COLUMN,
        materials = listOf(
            "INVERTED CLEAR FILTER COLUMN & COLLECTION FLASK",
            "LAYER 1: COARSE PEBBLES (TOP SEDIMENT TRAP)",
            "LAYER 2: FINE SILICA SAND (MICRO-PARTICLE TRAP)",
            "LAYER 3: ACTIVATED CHARCOAL & COTTON GAUZE BASE"
        ),
        buildSteps = listOf(
            "1. SECURE COTTON GAUZE AT THE BOTTOM NECK OF THE COLUMN.",
            "2. POUR ACTIVATED CHARCOAL FIRST, THEN FINE SAND, THEN COARSE PEBBLES.",
            "3. PLACE THE COLLECTION FLASK DIRECTLY UNDER THE OUTLET.",
            "4. POUR TURBID WATER SLOWLY TO OBSERVE MULTI-STAGE FILTRATION."
        ),
        scientificPrinciple = "PHYSICAL SIEVING & ADSORPTION: GRADED MEDIA TRAP SUSPENDED PARTICLES BY SIZE WHILE ACTIVATED CARBON ADSORBS DISSOLVED IMPURITIES.",
        baseTextEffects = listOf(
            "FILTRATION EFFECT: COARSE GRAVEL CAPTURES LEAVES AND GRIT AT THE TOP LAYER.",
            "PURIFICATION EFFECT: SAND AND ACTIVATED CHARCOAL CLARIFY CLOUDY WATER INTO CLEAR DROPLETS.",
            "FLOW EFFECT: STEADY GRAVITY PERCOLATION PREVENTS OVERFLOW."
        ),
        defaultAngleDeg = 90,
        angleLabel = "COLUMN TILT",
        driveLabel = "FLOW RATE"
    )
}

enum class CustomPrimitiveShape(val label: String) {
    SPHERE("SPHERE"),
    CUBE("BLOCK"),
    CYLINDER("CYLINDER"),
    RING("RING")
}

data class Custom3DPart(
    val id: String,
    val shape: CustomPrimitiveShape,
    val offsetX: Float,
    val offsetY: Float,
    val offsetZ: Float,
    val colorArgb: Long
) {
    val color: Color get() = Color(colorArgb)

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("shape", shape.name)
        put("x", offsetX.toDouble())
        put("y", offsetY.toDouble())
        put("z", offsetZ.toDouble())
        put("color", colorArgb)
    }

    companion object {
        fun fromJson(obj: JSONObject): Custom3DPart? {
            val id = obj.optString("id")
            if (id.isBlank()) return null
            val shape = CustomPrimitiveShape.entries.firstOrNull { it.name == obj.optString("shape") }
                ?: CustomPrimitiveShape.CUBE
            return Custom3DPart(
                id = id,
                shape = shape,
                offsetX = obj.optDouble("x", 0.0).toFloat(),
                offsetY = obj.optDouble("y", 0.0).toFloat(),
                offsetZ = obj.optDouble("z", 0.0).toFloat(),
                colorArgb = obj.optLong("color", 0xFF1E6B52)
            )
        }
    }
}

data class UserScienceTemplate(
    val id: String,
    val title: String,
    val domainTag: String,
    val baseChassis: CustomBaseChassis,
    val materials: List<String>,
    val buildSteps: List<String>,
    val scientificPrinciple: String,
    val expectedEffects: List<String>,
    val initialParts: List<Custom3DPart> = emptyList(),
    val defaultAngleDeg: Int = 45,
    val angleLabel: String = "MODEL ANGLE",
    val driveLabel: String = "POWER DRIVE"
) {
    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("title", title)
        put("domainTag", domainTag)
        put("baseChassis", baseChassis.id)
        put("materials", JSONArray(materials))
        put("buildSteps", JSONArray(buildSteps))
        put("scientificPrinciple", scientificPrinciple)
        put("expectedEffects", JSONArray(expectedEffects))
        val partsArr = JSONArray()
        initialParts.forEach { partsArr.put(it.toJson()) }
        put("initialParts", partsArr)
        put("defaultAngleDeg", defaultAngleDeg)
        put("angleLabel", angleLabel)
        put("driveLabel", driveLabel)
    }

    companion object {
        fun fromJson(obj: JSONObject): UserScienceTemplate? {
            val id = obj.optString("id")
            val title = obj.optString("title")
            if (id.isBlank() || title.isBlank()) return null
            val chassis = CustomBaseChassis.entries.firstOrNull { it.id == obj.optString("baseChassis") }
                ?: CustomBaseChassis.BLANK_WORKBENCH

            fun readStrings(key: String): List<String> {
                val arr = obj.optJSONArray(key) ?: return emptyList()
                return buildList {
                    for (i in 0 until arr.length()) {
                        val s = arr.optString(i).trim()
                        if (s.isNotEmpty()) add(s)
                    }
                }
            }

            val partsArr = obj.optJSONArray("initialParts") ?: JSONArray()
            val parts = buildList {
                for (i in 0 until partsArr.length()) {
                    partsArr.optJSONObject(i)?.let { Custom3DPart.fromJson(it) }?.let { add(it) }
                }
            }

            return UserScienceTemplate(
                id = id,
                title = title,
                domainTag = obj.optString("domainTag", "CUSTOM SCIENCE MODEL"),
                baseChassis = chassis,
                materials = readStrings("materials"),
                buildSteps = readStrings("buildSteps"),
                scientificPrinciple = obj.optString("scientificPrinciple", "USER-DEFINED EXPERIMENTAL MODEL."),
                expectedEffects = readStrings("expectedEffects"),
                initialParts = parts,
                defaultAngleDeg = obj.optInt("defaultAngleDeg", 45),
                angleLabel = obj.optString("angleLabel", "MODEL ANGLE"),
                driveLabel = obj.optString("driveLabel", "POWER DRIVE")
            )
        }

        fun listToJsonString(items: List<UserScienceTemplate>): String {
            val arr = JSONArray()
            items.forEach { arr.put(it.toJson()) }
            return arr.toString()
        }

        fun listFromJsonString(raw: String?): List<UserScienceTemplate> {
            if (raw.isNullOrBlank()) return emptyList()
            return try {
                val arr = JSONArray(raw)
                buildList {
                    for (i in 0 until arr.length()) {
                        arr.optJSONObject(i)?.let { fromJson(it) }?.let { add(it) }
                    }
                }
            } catch (_: Exception) {
                emptyList()
            }
        }
    }
}

data class ProjectDiagnostic(
    val code: String,
    val title: String,
    val detail: String,
    val fixLabel: String,
    val fixKey: String
)

data class ScienceProjectLabState(
    val template: ScienceModelTemplate? = ScienceModelTemplate.SOLAR_SYSTEM,
    val activeCustomTemplate: UserScienceTemplate? = null,
    val customProjectName: String = "",
    val yawDeg: Float = 28f,
    val pitchDeg: Float = 22f,
    val zoom: Float = 1.0f,
    val isExploded: Boolean = false,
    val showWireframe: Boolean = false,
    val customParts: List<Custom3DPart> = emptyList(),
    // Working & Error-Checker State
    val isPowerOrTriggerOn: Boolean = true,
    val isComponentAligned: Boolean = true,
    val isSealedOrWired: Boolean = false, // Start with 1 realistic error to discover & fix!
    val isSafetyGuardOn: Boolean = true,
    val driveLevel: Float = 0.72f,
    val primaryAngleDeg: Int = ScienceModelTemplate.SOLAR_SYSTEM.defaultAngleDeg,
    val hasRunCheck: Boolean = false
) {
    val activeTitle: String
        get() = customProjectName.trim().uppercase().ifBlank {
            activeCustomTemplate?.title ?: template?.title ?: "CUSTOM MODEL"
        }

    val activeCategoryTag: String
        get() = activeCustomTemplate?.domainTag ?: template?.categoryTag ?: "CUSTOM SCIENCE"

    val activeAccentColor: Color
        get() = template?.accentColor ?: Color(0xFF1E6B52)

    val effectiveChassis: CustomBaseChassis
        get() = activeCustomTemplate?.baseChassis ?: template?.chassis ?: CustomBaseChassis.BLANK_WORKBENCH

    val activeMaterials: List<String>
        get() = activeCustomTemplate?.materials ?: template?.materials ?: emptyList()

    val activeBuildSteps: List<String>
        get() = activeCustomTemplate?.buildSteps ?: template?.buildSteps ?: emptyList()

    val activePrinciple: String
        get() = activeCustomTemplate?.scientificPrinciple ?: template?.scientificPrinciple ?: ""

    val activeDriveLabel: String
        get() = activeCustomTemplate?.driveLabel ?: template?.driveLabel ?: "POWER DRIVE"

    val activeAngleLabel: String
        get() = activeCustomTemplate?.angleLabel ?: template?.angleLabel ?: "MODEL ANGLE"

    val activeDefaultAngleDeg: Int
        get() = activeCustomTemplate?.defaultAngleDeg ?: template?.defaultAngleDeg ?: 45

    fun evaluateErrors(): List<ProjectDiagnostic> {
        val issues = mutableListOf<ProjectDiagnostic>()
        if (!isPowerOrTriggerOn) {
            issues += ProjectDiagnostic(
                code = "ERR-01",
                title = "POWER / TRIGGER SWITCH IS OFF",
                detail = "MAIN ACTUATION SOURCE FOR $activeTitle IS CURRENTLY SWITCHED OFF.",
                fixLabel = "SWITCH ON",
                fixKey = "power"
            )
        }
        if (!isSealedOrWired) {
            val detail = when (effectiveChassis) {
                CustomBaseChassis.MOTOR_CIRCUIT -> "COPPER ROTOR CIRCUIT HAS AN OPEN WIRE LEAD TO THE BATTERY."
                CustomBaseChassis.HYDRAULIC_PISTON -> "HYDRAULIC TUBE SEAL HAS AN AIR LEAK AT THE PISTON NOZZLE."
                CustomBaseChassis.REACTION_CONE -> "CHAMBER BASE SEAL IS LOOSE — GAS PRESSURE ESCAPES DOWNWARD."
                CustomBaseChassis.TURBINE_TOWER -> "GENERATOR TERMINAL LEAD TO LED LOAD IS DISCONNECTED."
                CustomBaseChassis.OPTICS_TUBE -> "BOTTOM EYEPIECE SHUTTER OR BAFFLE SEAL IS UNSECURED."
                CustomBaseChassis.FILTER_COLUMN -> "LOWER GAUZE RETAINER IS UNLOCKED — SAND MEDIA MAY MIX."
                CustomBaseChassis.ORBITAL_RIG -> "CENTRAL POWER BUS & GEAR CLUTCH ARE DISENGAGED."
                CustomBaseChassis.BLANK_WORKBENCH -> "MAIN CONNECTOR LEAD OR BASE MOUNT FOR $activeTitle IS UNSEALED."
            }
            issues += ProjectDiagnostic(
                code = "ERR-02",
                title = "CONNECTION / SEAL ERROR",
                detail = detail,
                fixLabel = "FIX SEAL/WIRE",
                fixKey = "wire"
            )
        }
        if (!isComponentAligned) {
            issues += ProjectDiagnostic(
                code = "ERR-03",
                title = "MECHANICAL ALIGNMENT ERROR",
                detail = "CORE MOVING PARTS ARE MISALIGNED — CAUSING FRICTION OR BLOCKED PATH.",
                fixLabel = "ALIGN PARTS",
                fixKey = "align"
            )
        }
        if (!isSafetyGuardOn) {
            issues += ProjectDiagnostic(
                code = "ERR-04",
                title = "SAFETY / PROTECTIVE GUARD MISSING",
                detail = "ADD INSULATION, CATCH TRAY, OR OVERLOAD GUARD BEFORE RUNNING FULL OUTPUT.",
                fixLabel = "ENABLE GUARD",
                fixKey = "safety"
            )
        }
        if (driveLevel < 0.15f) {
            issues += ProjectDiagnostic(
                code = "ERR-05",
                title = "LOW $activeDriveLabel",
                detail = "$activeDriveLabel IS TOO LOW (${(driveLevel * 100).toInt()}%) TO SUSTAIN WORKING MOTION.",
                fixLabel = "BOOST DRIVE",
                fixKey = "drive"
            )
        }
        if (effectiveChassis == CustomBaseChassis.OPTICS_TUBE && primaryAngleDeg !in 40..50) {
            issues += ProjectDiagnostic(
                code = "ERR-06",
                title = "MIRROR ANGLE ERROR ($primaryAngleDeg°)",
                detail = "OPTICS MIRRORS MUST BE NEAR 45° SO THE LIGHT RAY HITS THE EYEPIECE CENTER.",
                fixLabel = "SET 45°",
                fixKey = "angle"
            )
        }
        if (effectiveChassis == CustomBaseChassis.TURBINE_TOWER && primaryAngleDeg !in 18..55) {
            issues += ProjectDiagnostic(
                code = "ERR-06",
                title = "STALLED BLADE PITCH ($primaryAngleDeg°)",
                detail = "BLADE PITCH OUTSIDE 18°–55° CANNOT CAPTURE OPTIMAL WIND TORQUE.",
                fixLabel = "SET 32°",
                fixKey = "angle"
            )
        }
        if (effectiveChassis == CustomBaseChassis.BLANK_WORKBENCH && customParts.isEmpty()) {
            issues += ProjectDiagnostic(
                code = "ERR-07",
                title = "EMPTY 3D WORKBENCH",
                detail = "YOUR CUSTOM TEMPLATE USES A BLANK WORKBENCH WITH 0 3D PARTS. ADD PARTS IN PART 2 OR TAP FIX.",
                fixLabel = "ADD PART",
                fixKey = "add_part"
            )
        }
        return issues
    }

    fun computeDynamicTextEffects(): List<String> {
        val errors = evaluateErrors()
        val statusSummary = if (errors.isEmpty()) {
            "LIVE STATUS: PROJECT IS WORKING SMOOTHLY AT ${(driveLevel * 100).toInt()}% $activeDriveLabel WITH 0 ERRORS."
        } else {
            "LIVE STATUS: ${errors.size} ISSUE(S) BLOCKING FULL OUTPUT — CHECK PART 3 TO MAKE IT WORKING."
        }
        val angleEffect = "GEOMETRY EFFECT: $activeAngleLabel SET TO $primaryAngleDeg° • CUSTOM 3D PARTS: ${customParts.size}."
        val baseEffects = activeCustomTemplate?.expectedEffects?.ifEmpty {
            listOf(
                "CUSTOM EFFECT: $activeTitle RESPONDS TO ${(driveLevel * 100).toInt()}% $activeDriveLabel.",
                "STRUCTURAL EFFECT: BUILT ON ${effectiveChassis.label} WITH ${activeMaterials.size} MATERIAL(S)."
            )
        } ?: template?.baseTextEffects ?: emptyList()
        return listOf(statusSummary, angleEffect) + baseEffects
    }
}
