package com.example.data.mock

import com.example.data.model.*

object MockData {

    fun createStandardChecklist(): List<ChecklistItem> {
        return listOf(
            ChecklistItem(
                id = "CHK-01",
                section = ChecklistSection.BASIC_VERIFICATION,
                question = "Are the statutory industrial registrations, operating licenses and DOJS approvals visibly displayed at entry?",
                statutoryReference = "Factories Act Sec 7(1) & DOJS Reg. 2024/09",
                isEvidenceRequired = true,
                status = ComplianceStatus.COMPLIANT,
                remarks = "Official registration certificate valid till Dec 2027 displayed in reception area."
            ),
            ChecklistItem(
                id = "CHK-02",
                section = ChecklistSection.BASIC_VERIFICATION,
                question = "Is the authorized site engineer/safety officer present with valid credentials and statutory appointment letter?",
                statutoryReference = "National Safety Code Cl. 4.2",
                isEvidenceRequired = false,
                status = ComplianceStatus.COMPLIANT,
                remarks = "Chief Safety Officer Er. P. K. Verma present on-site with valid appointment records."
            ),
            ChecklistItem(
                id = "CHK-03",
                section = ChecklistSection.SITE_INFRASTRUCTURE,
                question = "Are emergency exit routes, assembly points and evacuation corridors unobstructed and clearly demarcated?",
                statutoryReference = "National Building Code 2016 Part 4",
                isEvidenceRequired = true,
                status = ComplianceStatus.PARTIALLY_COMPLIANT,
                remarks = "Exit corridor near Bay C partially blocked by empty packing pallets. Immediate clearance instructed."
            ),
            ChecklistItem(
                id = "CHK-04",
                section = ChecklistSection.SITE_INFRASTRUCTURE,
                question = "Are hazardous chemical storage zones equipped with secondary containment bunds and mandatory HAZCHEM signage?",
                statutoryReference = "Manufacture & Storage of Hazardous Chemicals Rules 1989",
                isEvidenceRequired = true,
                status = ComplianceStatus.COMPLIANT,
                remarks = "Bund capacity conforms to 110% storage volume standard. Hazchem signs prominent."
            ),
            ChecklistItem(
                id = "CHK-05",
                section = ChecklistSection.SAFETY_COMPLIANCE,
                question = "Is the active fire-fighting equipment (hydrants, extinguishers, suppression systems) tested within statutory validity?",
                statutoryReference = "IS 2190 & Fire Service Act Regulations",
                isEvidenceRequired = true,
                status = ComplianceStatus.COMPLIANT,
                remarks = "Hydrant pressure logged at 7.2 bar. Extinguishers hydro-tested June 2026."
            ),
            ChecklistItem(
                id = "CHK-06",
                section = ChecklistSection.SAFETY_COMPLIANCE,
                question = "Are industrial workers equipped with mandatory PPE (helmets, high-vis vests, steel-toe boots, ear protection)?",
                statutoryReference = "Occupational Safety, Health & Working Conditions Code",
                isEvidenceRequired = true,
                status = ComplianceStatus.NON_COMPLIANT,
                remarks = "3 contract technicians in fabrication bay observed without protective goggles and ear defenders."
            ),
            ChecklistItem(
                id = "CHK-07",
                section = ChecklistSection.SAFETY_COMPLIANCE,
                question = "Are high-voltage electrical panels safely earthed, double-insulated and equipped with danger notices & rubber mats?",
                statutoryReference = "Central Electricity Authority Safety Reg. 2010",
                isEvidenceRequired = true,
                status = ComplianceStatus.COMPLIANT,
                remarks = "Double earthing verified with resistance log < 1.0 Ohm. Class A insulation mats placed."
            ),
            ChecklistItem(
                id = "CHK-08",
                section = ChecklistSection.DOCUMENTATION,
                question = "Are statutory accident registers, mock drill records and first-aid replenishment logs up-to-date?",
                statutoryReference = "State Factory Rules Form 24",
                isEvidenceRequired = false,
                status = ComplianceStatus.COMPLIANT,
                remarks = "Last biannual emergency evacuation drill conducted on 15 July 2026."
            ),
            ChecklistItem(
                id = "CHK-09",
                section = ChecklistSection.DOCUMENTATION,
                question = "Is the State Pollution Control Board (SPCB) Consent to Operate (CTO) valid and emission logs recorded daily?",
                statutoryReference = "Air & Water (Prevention & Control of Pollution) Acts",
                isEvidenceRequired = true,
                status = ComplianceStatus.COMPLIANT,
                remarks = "CTO valid through March 2028. Continuous emission monitoring system active."
            ),
            ChecklistItem(
                id = "CHK-10",
                section = ChecklistSection.FINAL_ASSESSMENT,
                question = "Are workplace hygiene, potable drinking water test certificates and sanitary facilities compliant with standards?",
                statutoryReference = "Statutory Welfare Amenities Act Sec 18",
                isEvidenceRequired = false,
                status = ComplianceStatus.COMPLIANT,
                remarks = "NABL water quality test certificate dated 10 Aug 2026 verified."
            )
        )
    }

    fun getInitialInspections(): List<Inspection> {
        val standardChecklist = createStandardChecklist()

        return listOf(
            Inspection(
                id = "INS-2026-00124",
                siteName = "Apex Petrochemical Complex",
                organizationName = "Apex Industrial Hydrocarbons Ltd.",
                inspectionType = "Periodic Statutory Safety & Environmental Audit",
                fullAddress = "Plot 42-45, Phase III, Okhla Industrial Area, New Delhi - 110020",
                coordinates = "28.5355° N, 77.2690° E",
                scheduledDate = "01 Sep 2026",
                scheduledTime = "10:30 AM",
                priority = Priority.HIGH,
                status = InspectionStatus.IN_PROGRESS,
                assignedTeam = "Central Northern Taskforce #04",
                assignedInspector = "Rajesh Sharma (GOI-DOJS-8924)",
                instructions = "Conduct comprehensive statutory safety inspection. Verify compliance on HAZMAT protocols, effluent discharge, and emergency exit clearance.",
                specialNotes = "High priority statutory directive from Joint Secretary Office. Cross-verify CCTV log entries for safety protocol adherence.",
                startTime = "10:35 AM",
                checklistItems = standardChecklist,
                observations = listOf(
                    Observation(
                        id = "OBS-01",
                        inspectionId = "INS-2026-00124",
                        title = "Temporary pallet storage in Bay C egress corridor",
                        category = "Infrastructure",
                        severity = Severity.MEDIUM,
                        description = "Wooden pallets stacked to a height of 2.1m encroaching by 40% onto the designated emergency egress pathway.",
                        timestamp = "01 Sep 2026, 11:15 AM",
                        linkedEvidenceIds = listOf("EVD-01")
                    ),
                    Observation(
                        id = "OBS-02",
                        inspectionId = "INS-2026-00124",
                        title = "Pressure gauge calibration sticker due in Tank Zone 4",
                        category = "Compliance",
                        severity = Severity.LOW,
                        description = "Nitrogen purge line gauge sticker shows calibration expired last week. Re-certification scheduled for tomorrow as per records.",
                        timestamp = "01 Sep 2026, 11:42 AM",
                        linkedEvidenceIds = emptyList()
                    )
                ),
                violations = listOf(
                    Violation(
                        id = "VIO-01",
                        inspectionId = "INS-2026-00124",
                        violationType = "Non-provision of required PPE to contract workforce",
                        severity = Severity.HIGH,
                        description = "Contract technicians engaged in high-decibel cutting and grinding without certified ear defenders and eye shielding.",
                        relevantChecklistItem = "Are industrial workers equipped with mandatory PPE?",
                        recommendedAction = "Immediate stoppage of grinding operations until complete certified PPE kit issuance and safety briefing.",
                        correctiveDeadline = "03 Sep 2026",
                        timestamp = "01 Sep 2026, 11:50 AM",
                        linkedEvidenceIds = listOf("EVD-02")
                    )
                ),
                evidenceList = listOf(
                    Evidence(
                        id = "EVD-01",
                        inspectionId = "INS-2026-00124",
                        checklistItemId = "CHK-03",
                        title = "Bay C Egress Corridor Obstruction",
                        type = "PHOTO",
                        timestamp = "01 Sep 2026 11:14 AM",
                        location = "Bay C - Fabrication Section, Okhla Ph-III",
                        coordinates = "28.5356° N, 77.2692° E",
                        description = "Photographic proof of stacked pallets encroaching upon emergency route."
                    ),
                    Evidence(
                        id = "EVD-02",
                        inspectionId = "INS-2026-00124",
                        checklistItemId = "CHK-06",
                        title = "PPE Non-Compliance in Grinding Area",
                        type = "PHOTO",
                        timestamp = "01 Sep 2026 11:48 AM",
                        location = "Workshop Floor 2, Okhla Ph-III",
                        coordinates = "28.5354° N, 77.2689° E",
                        description = "Geotagged frame showing unshielded contract workers."
                    ),
                    Evidence(
                        id = "EVD-03",
                        inspectionId = "INS-2026-00124",
                        checklistItemId = "CHK-05",
                        title = "Fire Hydrant Pressure Meter Reading",
                        type = "PHOTO",
                        timestamp = "01 Sep 2026 12:05 PM",
                        location = "Main Pump House, Okhla Ph-III",
                        coordinates = "28.5358° N, 77.2695° E",
                        description = "Hydrant manifold test display reading 7.2 bar."
                    )
                ),
                finalRemarks = "Facility exhibits robust core infrastructure and valid statutory approvals. Immediate corrective intervention mandated for contract worker PPE protocols and aisle clearances.",
                recommendation = "Conditional approval granted subject to compliance submission for VIO-01 within 48 hours."
            ),
            Inspection(
                id = "INS-2026-00125",
                siteName = "Bharat Logistics & Cold Storage Hub",
                organizationName = "Bharat Warehousing & Logistics Corp",
                inspectionType = "Fire Safety & Cold Storage Ammonia Audit",
                fullAddress = "Sector 14, Bhiwandi Logistics Corridor, Thane, Maharashtra - 421302",
                coordinates = "19.2967° N, 73.0631° E",
                scheduledDate = "01 Sep 2026",
                scheduledTime = "02:00 PM",
                priority = Priority.HIGH,
                status = InspectionStatus.PENDING,
                instructions = "Inspect ammonia refrigeration plant safety valves, emergency water deluge showers, and thermal insulation barriers.",
                specialNotes = "Check mandatory quarterly leak detector sensor calibration logs."
            ),
            Inspection(
                id = "INS-2026-00126",
                siteName = "Hindustan Heavy Engineering Plant",
                organizationName = "Hindustan Heavy Machinery Ltd.",
                inspectionType = "Structural Integrity & Heavy Crane Machinery Audit",
                fullAddress = "8th Main Road, Peenya Industrial Area Phase II, Bengaluru, Karnataka - 560058",
                coordinates = "13.0312° N, 77.5186° E",
                scheduledDate = "02 Sep 2026",
                scheduledTime = "09:30 AM",
                priority = Priority.MEDIUM,
                status = InspectionStatus.PENDING,
                instructions = "Review 50-tonne EOT crane load test certificates, wire rope integrity logs, and overhead walkway toe-boards."
            ),
            Inspection(
                id = "INS-2026-00127",
                siteName = "National Pharma Labs & Research Unit",
                organizationName = "National Biologicals & Life Sciences Pvt Ltd",
                inspectionType = "Bio-Safety Level 3 & Hazardous Waste Protocol",
                fullAddress = "Plot 18, Genome Valley, Shamirpet, Hyderabad, Telangana - 500078",
                coordinates = "17.6085° N, 78.5833° E",
                scheduledDate = "02 Sep 2026",
                scheduledTime = "01:30 PM",
                priority = Priority.HIGH,
                status = InspectionStatus.PENDING,
                instructions = "Verify HEPA filtration differential pressure monitors, bio-medical waste autoclave sterilization records, and access control logs."
            ),
            Inspection(
                id = "INS-2026-00128",
                siteName = "Southern Tech & Manufacturing Park",
                organizationName = "Southern Precision Electronics Ltd.",
                inspectionType = "Cleanroom Compliance & Chemical Discharge Audit",
                fullAddress = "SIPCOT Industrial Complex, Sriperumbudur, Tamil Nadu - 602105",
                coordinates = "12.9699° N, 79.9405° E",
                scheduledDate = "03 Sep 2026",
                scheduledTime = "11:00 AM",
                priority = Priority.LOW,
                status = InspectionStatus.PENDING,
                instructions = "Assess ESD ground grid dissipation values and PCB etching chemical neutralization plant."
            ),
            Inspection(
                id = "INS-2026-00129",
                siteName = "Gujarat Chemical Manufacturing Unit 4",
                organizationName = "Gujarat Agrochem Industries Ltd.",
                inspectionType = "Major Accident Hazard (MAH) Site Audit",
                fullAddress = "Plot 112, GIDC Industrial Estate, Ankleshwar, Gujarat - 393002",
                coordinates = "21.6284° N, 73.0039° E",
                scheduledDate = "29 Aug 2026",
                scheduledTime = "10:00 AM",
                priority = Priority.HIGH,
                status = InspectionStatus.COMPLETED,
                instructions = "Audit chlorine storage emergency scrubber systems and on-site disaster management mock drill."
            ),
            Inspection(
                id = "INS-2026-00130",
                siteName = "Eastern Power Grid Substation 33KV",
                organizationName = "Eastern Power Distribution Utility Ltd",
                inspectionType = "Substation Electrical Safety & Earthing Audit",
                fullAddress = "Sector V, Bidhannagar, Salt Lake, Kolkata, West Bengal - 700091",
                coordinates = "22.5804° N, 88.4378° E",
                scheduledDate = "28 Aug 2026",
                scheduledTime = "03:00 PM",
                priority = Priority.MEDIUM,
                status = InspectionStatus.COMPLETED,
                instructions = "Inspect transformer oil dielectric strength, SF6 gas pressure switches, and perimeter fire barriers."
            ),
            Inspection(
                id = "INS-2026-00131",
                siteName = "Delhi Metro Rail Maintenance Depot",
                organizationName = "Delhi Metro Rail Corporation Ltd",
                inspectionType = "Rolling Stock Maintenance & Stabling Safety",
                fullAddress = "Khyber Pass Depot, Civil Lines, New Delhi - 110054",
                coordinates = "28.6853° N, 77.2274° E",
                scheduledDate = "27 Aug 2026",
                scheduledTime = "09:00 AM",
                priority = Priority.HIGH,
                status = InspectionStatus.COMPLETED,
                instructions = "Evaluate traction power isolation protocols and train inspection pit ventilation."
            ),
            Inspection(
                id = "INS-2026-00132",
                siteName = "Central Food Grain Mega Silos",
                organizationName = "Food Corporation of India Storage Facility",
                inspectionType = "Pest Fumigation Safety & Grain Silo Dust Hazard",
                fullAddress = "GT Karnal Road, Kundli Industrial Area, Sonipat, Haryana - 131028",
                coordinates = "28.8789° N, 77.1264° E",
                scheduledDate = "26 Aug 2026",
                scheduledTime = "10:30 AM",
                priority = Priority.MEDIUM,
                status = InspectionStatus.COMPLETED,
                instructions = "Verify grain dust explosion venting panels and phosphine gas fumigation safety protocols."
            ),
            Inspection(
                id = "INS-2026-00133",
                siteName = "Noida Semiconductor Fabrication Facility",
                organizationName = "Indus Microelectronics Tech Ltd",
                inspectionType = "Special Gas Cylinder Bunker & Ultrapure Water Audit",
                fullAddress = "Block B, Sector 62, Noida, Uttar Pradesh - 201309",
                coordinates = "28.6280° N, 77.3649° E",
                scheduledDate = "25 Aug 2026",
                scheduledTime = "02:00 PM",
                priority = Priority.HIGH,
                status = InspectionStatus.COMPLETED,
                instructions = "Inspect silane gas automatic shut-off valves and cleanroom emergency evacuation interlocks."
            ),
            Inspection(
                id = "INS-2026-00134",
                siteName = "Chakan Auto Components Foundry",
                organizationName = "Chakan Precision Castings Pvt Ltd",
                inspectionType = "Foundry Molten Metal & Fume Extraction Audit",
                fullAddress = "Phase II, MIDC Chakan, Pune, Maharashtra - 410501",
                coordinates = "18.7562° N, 73.8567° E",
                scheduledDate = "03 Sep 2026",
                scheduledTime = "03:30 PM",
                priority = Priority.MEDIUM,
                status = InspectionStatus.PENDING,
                instructions = "Assess cupola furnace emission controls and ladle transport crane load testing records."
            ),
            Inspection(
                id = "INS-2026-00135",
                siteName = "Gurugram High-Rise Commercial Complex",
                organizationName = "DLF Commercial Assets Ltd",
                inspectionType = "High-Rise Fire Refuge Floor & Smoke Evacuation Audit",
                fullAddress = "Sector 58, Golf Course Extension Road, Gurugram, Haryana - 122102",
                coordinates = "28.4089° N, 77.1025° E",
                scheduledDate = "04 Sep 2026",
                scheduledTime = "11:30 AM",
                priority = Priority.LOW,
                status = InspectionStatus.PENDING,
                instructions = "Verify pressurization of fire staircases and automated sprinkler flow alarms."
            )
        )
    }

    fun getInitialReports(): List<Report> {
        return listOf(
            Report(
                id = "REP-2026-0891",
                inspectionId = "INS-2026-00129",
                siteName = "Gujarat Chemical Manufacturing Unit 4",
                organizationName = "Gujarat Agrochem Industries Ltd.",
                location = "Ankleshwar GIDC, Gujarat",
                inspectionDate = "29 Aug 2026",
                submissionDate = "29 Aug 2026, 04:30 PM",
                inspectorName = "Rajesh Sharma",
                status = "Approved",
                compliantCount = 9,
                partialCount = 1,
                nonCompliantCount = 0,
                violationsCount = 0,
                observationsCount = 2,
                recommendation = "Full statutory compliance confirmed. Automatic chlorine scrubber systems tested functional with < 15s response time."
            ),
            Report(
                id = "REP-2026-0887",
                inspectionId = "INS-2026-00130",
                siteName = "Eastern Power Grid Substation 33KV",
                organizationName = "Eastern Power Distribution Utility Ltd",
                location = "Salt Lake Sector V, Kolkata",
                inspectionDate = "28 Aug 2026",
                submissionDate = "28 Aug 2026, 05:15 PM",
                inspectorName = "Rajesh Sharma",
                status = "Approved",
                compliantCount = 10,
                partialCount = 0,
                nonCompliantCount = 0,
                violationsCount = 0,
                observationsCount = 1,
                recommendation = "Station earthing mesh impedance measured at 0.42 Ohms. Full statutory compliance approved."
            ),
            Report(
                id = "REP-2026-0883",
                inspectionId = "INS-2026-00131",
                siteName = "Delhi Metro Rail Maintenance Depot",
                organizationName = "Delhi Metro Rail Corporation Ltd",
                location = "Khyber Pass Depot, Civil Lines, New Delhi",
                inspectionDate = "27 Aug 2026",
                submissionDate = "27 Aug 2026, 02:45 PM",
                inspectorName = "Rajesh Sharma",
                status = "Approved",
                compliantCount = 8,
                partialCount = 2,
                nonCompliantCount = 0,
                violationsCount = 0,
                observationsCount = 3,
                recommendation = "Depot traction safety interlocks verified. Rectification of pit drainage grates instructed within 14 days."
            ),
            Report(
                id = "REP-2026-0879",
                inspectionId = "INS-2026-00132",
                siteName = "Central Food Grain Mega Silos",
                organizationName = "Food Corporation of India",
                location = "Kundli, Sonipat, Haryana",
                inspectionDate = "26 Aug 2026",
                submissionDate = "26 Aug 2026, 04:00 PM",
                inspectorName = "Rajesh Sharma",
                status = "Returned",
                compliantCount = 6,
                partialCount = 2,
                nonCompliantCount = 2,
                violationsCount = 2,
                observationsCount = 4,
                recommendation = "Returned for re-audit due to non-availability of annual calibration certificate for phosphine gas sensors."
            ),
            Report(
                id = "REP-2026-0874",
                inspectionId = "INS-2026-00133",
                siteName = "Noida Semiconductor Fabrication Facility",
                organizationName = "Indus Microelectronics Tech Ltd",
                location = "Sector 62, Noida, Uttar Pradesh",
                inspectionDate = "25 Aug 2026",
                submissionDate = "25 Aug 2026, 06:10 PM",
                inspectorName = "Rajesh Sharma",
                status = "Approved",
                compliantCount = 10,
                partialCount = 0,
                nonCompliantCount = 0,
                violationsCount = 0,
                observationsCount = 1,
                recommendation = "State-of-the-art cleanroom fire interlock and emergency silane toxic gas scrubbing certified fully compliant."
            ),
            Report(
                id = "REP-2026-0870",
                inspectionId = "INS-2026-00120",
                siteName = "Indraprastha Gas Distribution Terminal",
                organizationName = "Indraprastha Gas Limited",
                location = "Sarita Vihar, New Delhi",
                inspectionDate = "22 Aug 2026",
                submissionDate = "22 Aug 2026, 03:20 PM",
                inspectorName = "Rajesh Sharma",
                status = "Draft",
                compliantCount = 7,
                partialCount = 2,
                nonCompliantCount = 1,
                violationsCount = 1,
                observationsCount = 2,
                recommendation = "Draft inspection findings compiled. Pending upload of signed Joint Declaration with site head."
            )
        )
    }

    fun getInitialNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem(
                id = "NOTIF-01",
                title = "New Inspection Assigned",
                message = "Joint Secretary Office has assigned statutory inspection for Apex Petrochemical Complex (INS-2026-00124).",
                timestamp = "10 minutes ago",
                type = NotificationType.NEW_ASSIGNMENT,
                isRead = false,
                relatedInspectionId = "INS-2026-00124"
            ),
            NotificationItem(
                id = "NOTIF-02",
                title = "Inspection Due Tomorrow",
                message = "Scheduled inspection for Hindustan Heavy Engineering Plant (INS-2026-00126) is due on 02 Sep 2026 at 09:30 AM.",
                timestamp = "1 hour ago",
                type = NotificationType.DUE_TOMORROW,
                isRead = false,
                relatedInspectionId = "INS-2026-00126"
            ),
            NotificationItem(
                id = "NOTIF-03",
                title = "Critical Violation Alert",
                message = "High severity violation recorded for non-provision of safety PPE in Okhla Phase III complex. Escalated to Zonal Director.",
                timestamp = "2 hours ago",
                type = NotificationType.CRITICAL_ALERT,
                isRead = false,
                relatedInspectionId = "INS-2026-00124"
            ),
            NotificationItem(
                id = "NOTIF-04",
                title = "Inspection Report Approved",
                message = "Statutory Inspection Report REP-2026-0891 for Gujarat Agrochem Industries has been approved by DOJS Directorate.",
                timestamp = "Yesterday, 05:40 PM",
                type = NotificationType.APPROVED,
                isRead = true,
                relatedInspectionId = "INS-2026-00129"
            ),
            NotificationItem(
                id = "NOTIF-05",
                title = "Report Returned for Review",
                message = "Report REP-2026-0879 (Central Food Grain Mega Silos) returned for re-verification of calibration certificates.",
                timestamp = "28 Aug 2026",
                type = NotificationType.RETURNED,
                isRead = true,
                relatedInspectionId = "INS-2026-00132"
            ),
            NotificationItem(
                id = "NOTIF-06",
                title = "System Directive 2026/GIMS-44",
                message = "All field inspection teams must ensure geotagged photographic evidence with timestamp watermark for hazardous units.",
                timestamp = "27 Aug 2026",
                type = NotificationType.SYSTEM,
                isRead = true
            ),
            NotificationItem(
                id = "NOTIF-07",
                title = "Monthly Data Synchronization",
                message = "Local offline database cache synchronized successfully with National Central Server.",
                timestamp = "26 Aug 2026",
                type = NotificationType.SYSTEM,
                isRead = true
            ),
            NotificationItem(
                id = "NOTIF-08",
                title = "Team Schedule Update",
                message = "Taskforce roster for Northern Zone updated for September 2026. Review assigned jurisdiction in profile.",
                timestamp = "25 Aug 2026",
                type = NotificationType.SYSTEM,
                isRead = true
            )
        )
    }

    fun getTeamMembers(): List<TeamMember> {
        return listOf(
            TeamMember(
                id = "TM-01",
                name = "Dr. Arvind Swaminathan",
                designation = "Zonal Deputy Director & Lead Auditor",
                role = "Team Leader",
                status = "Active",
                phone = "+91 98101 23456",
                email = "arvind.swaminathan@dojs.gov.in",
                completedInspections = 142
            ),
            TeamMember(
                id = "TM-02",
                name = "Rajesh Sharma",
                designation = "Senior Field Inspection Officer",
                role = "Field Inspector",
                status = "On Field",
                phone = "+91 98765 43210",
                email = "inspector.demo@dojs.gov.in",
                completedInspections = 98
            ),
            TeamMember(
                id = "TM-03",
                name = "Er. Sunita Narain",
                designation = "Hazardous Chemical Specialist",
                role = "Technical Inspector",
                status = "Active",
                phone = "+91 97112 34567",
                email = "sunita.narain@dojs.gov.in",
                completedInspections = 84
            ),
            TeamMember(
                id = "TM-04",
                name = "Vikas Deshmukh",
                designation = "Statutory Legal & Compliance Officer",
                role = "Documentation Officer",
                status = "Active",
                phone = "+91 94220 98765",
                email = "vikas.deshmukh@dojs.gov.in",
                completedInspections = 115
            ),
            TeamMember(
                id = "TM-05",
                name = "Priya Sundaram",
                designation = "Electrical & Structural Safety Engineer",
                role = "Technical Inspector",
                status = "On Field",
                phone = "+91 99401 56789",
                email = "priya.sundaram@dojs.gov.in",
                completedInspections = 67
            )
        )
    }
}
