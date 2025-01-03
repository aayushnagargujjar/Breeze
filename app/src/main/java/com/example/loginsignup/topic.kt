package com.example.loginsignup

data class Topic(val id: String, val name: String, val subtopics: List<Subtopic>)

data class Subtopic(val id: String, val name: String)

fun getSupportedTopics(): ArrayList<Topic> {
    return ArrayList(listOf(
        Topic("business", "Business", listOf(
            Subtopic("cryptocurrency", "Cryptocurrency"),
            Subtopic("economy", "Economy"),
            Subtopic("finance", "Finance"),
            Subtopic("forex", "Forex"),
            Subtopic("markets", "Markets"),
            Subtopic("property", "Property"),
            Subtopic("startup", "Startup")
        )),
        Topic("entertainment", "Entertainment", listOf(
            Subtopic("arts", "Arts"),
            Subtopic("books", "Books"),
            Subtopic("celebrities", "Celebrities"),
            Subtopic("gaming", "Gaming"),
            Subtopic("movies", "Movies"),
            Subtopic("music", "Music"),
            Subtopic("tv", "TV")
        )),
        Topic("general", "General", emptyList()),
        Topic("health", "Health", listOf(
            Subtopic("disease", "Disease"),
            Subtopic("fitness", "Fitness"),
            Subtopic("medication", "Medication"),
            Subtopic("publichealth", "Public Health")
        )),
        Topic("lifestyle", "Lifestyle", listOf(
            Subtopic("autos", "Autos"),
            Subtopic("beauty", "Beauty"),
            Subtopic("cooking", "Cooking"),
            Subtopic("fashion", "Fashion"),
            Subtopic("religion", "Religion"),
            Subtopic("tourism", "Tourism"),
            Subtopic("transportation", "Transportation"),
            Subtopic("travel", "Travel")
        )),
        Topic("politics", "Politics", listOf(
            Subtopic("government", "Government"),
            Subtopic("humanrights", "Human Rights"),
            Subtopic("infrastructure", "Infrastructure"),
            Subtopic("policy", "Policy")
        )),
        Topic("science", "Science", listOf(
            Subtopic("climate", "Climate"),
            Subtopic("education", "Education"),
            Subtopic("energy", "Energy"),
            Subtopic("environment", "Environment"),
            Subtopic("genetics", "Genetics"),
            Subtopic("geology", "Geology"),
            Subtopic("physics", "Physics"),
            Subtopic("space", "Space"),
            Subtopic("wildlife", "Wildlife")
        )),
        Topic("sports", "Sports", listOf(
            Subtopic("baseball", "Baseball"),
            Subtopic("basketball", "Basketball"),
            Subtopic("boxing", "Boxing"),
            Subtopic("cricket", "Cricket"),
            Subtopic("esports", "Esports"),
            Subtopic("f1", "F1"),
            Subtopic("football", "Football"),
            Subtopic("golf", "Golf"),
            Subtopic("hockey", "Hockey"),
            Subtopic("nascar", "Nascar"),
            Subtopic("rugby", "Rugby"),
            Subtopic("soccer", "Soccer"),
            Subtopic("tennis", "Tennis"),
            Subtopic("volleyball", "Volleyball")
        )),
        Topic("technology", "Technology", listOf(
            Subtopic("ai", "AI"),
            Subtopic("computing", "Computing"),
            Subtopic("cybersec", "Cybersecurity"),
            Subtopic("gadgets", "Gadgets"),
            Subtopic("internet", "Internet"),
            Subtopic("mobile", "Mobile"),
            Subtopic("robot", "Robot"),
            Subtopic("vr", "VR")
        )),
        Topic("world", "World", listOf(
            Subtopic("culture", "Culture"),
            Subtopic("history", "History")
        ))
    ))
}
