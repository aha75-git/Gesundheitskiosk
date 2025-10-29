import FeatureCard from "./FeatureCard.tsx";

export default function FeaturesSection() {
    return (
        <section className="features">
            <div className="container">
                <h2>Warum uns wählen?</h2>
                <div className="features-grid">

                    <FeatureCard isFontawesome={true} icon={"fa-solid fa-headset"} title={"Chat Soforthilfe"}
                                 description={"Echtzeit-Hilfe für ihre Fragen und Anliegen"} />

                    <FeatureCard isFontawesome={true} icon={"fas fa-search"} title={"Einfache Suche"}
                                 description={"Finden Sie qualifizierte Berater basierend auf Ihren Bedürfnissen"} />

                    <FeatureCard isFontawesome={true} icon={"fas fa-calendar-check"} title={"Termin vereinbaren"}
                                 description={"Termine mit nur wenigen Klicks vereinbaren"} />

                    <FeatureCard isFontawesome={true} icon={"fas fa-shield-alt"} title={"Sicher"}
                                 description={"Ihre Daten und Privatsphäre sind geschützt"} />

                </div>
            </div>
        </section>
    )
}