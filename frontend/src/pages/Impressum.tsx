export default function Impressum () {
    return (
        <div className="legal-page">
            <div className="container">
                <div className="legal-header">
                    <h1 className="page-title">Impressum</h1>
                    <p className="page-subtitle">Angaben gemäß § 5 TMG</p>
                </div>

                <div className="legal-content">
                    <div className="legal-section">
                        <h2>Betreiber und Kontakt</h2>
                        <div className="contact-info">
                            <p>
                                <strong>Gesundheitslotse gGmbH</strong><br />
                                Musterstraße 123<br />
                                10115 Berlin<br />
                                Deutschland
                            </p>

                            <p>
                                <strong>Vertreten durch:</strong><br />
                                Geschäftsführer: Max Mustermann<br />
                                Registergericht: Amtsgericht Berlin-Charlottenburg<br />
                                Registernummer: HRB 12345
                            </p>

                            <p>
                                <strong>Kontakt:</strong><br />
                                Telefon: +49 (0) 800 - GESUND (437863)<br />
                                E-Mail: info@gesundheitslotse.de<br />
                                Website: https://gesundheitslotse.de
                            </p>
                        </div>
                    </div>

                    <div className="legal-section">
                        <h2>Umsatzsteuer-Identifikationsnummer</h2>
                        <p>
                            Umsatzsteuer-Identifikationsnummer gemäß §27 a Umsatzsteuergesetz:<br />
                            DE 123 456 789
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>Verbraucherstreitbeilegung / Universalschlichtungsstelle</h2>
                        <p>
                            Wir sind nicht bereit oder verpflichtet, an Streitbeilegungsverfahren vor einer
                            Verbraucherschlichtungsstelle teilzunehmen.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>Haftung für Inhalte</h2>
                        <p>
                            Als Diensteanbieter sind wir gemäß § 7 Abs.1 TMG für eigene Inhalte auf diesen Seiten
                            nach den allgemeinen Gesetzen verantwortlich. Nach §§ 8 bis 10 TMG sind wir als
                            Diensteanbieter jedoch nicht verpflichtet, übermittelte oder gespeicherte fremde
                            Informationen zu überwachen oder nach Umständen zu forschen, die auf eine rechtswidrige
                            Tätigkeit hinweisen.
                        </p>
                        <p>
                            Verpflichtungen zur Entfernung oder Sperrung der Nutzung von Informationen nach den
                            allgemeinen Gesetzen bleiben hiervon unberührt. Eine diesbezügliche Haftung ist jedoch
                            erst ab dem Zeitpunkt der Kenntnis einer konkreten Rechtsverletzung möglich. Bei
                            Bekanntwerden von entsprechenden Rechtsverletzungen werden wir diese Inhalte umgehend
                            entfernen.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>Haftung für Links</h2>
                        <p>
                            Unser Angebot enthält Links zu externen Websites Dritter, auf deren Inhalte wir keinen
                            Einfluss haben. Deshalb können wir für diese fremden Inhalte auch keine Gewähr übernehmen.
                            Für die Inhalte der verlinkten Seiten ist stets der jeweilige Anbieter oder Betreiber
                            der Seiten verantwortlich.
                        </p>
                        <p>
                            Die verlinkten Seiten wurden zum Zeitpunkt der Verlinkung auf mögliche Rechtsverstöße
                            überprüft. Rechtswidrige Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar.
                        </p>
                        <p>
                            Eine permanente inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete
                            Anhaltspunkte einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden von
                            Rechtsverletzungen werden wir derartige Links umgehend entfernen.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>Urheberrecht</h2>
                        <p>
                            Die durch die Seitenbetreiber erstellten Inhalte und Werke auf diesen Seiten unterliegen
                            dem deutschen Urheberrecht. Die Vervielfältigung, Bearbeitung, Verbreitung und jede
                            Art der Verwertung außerhalb der Grenzen des Urheberrechtes bedürfen der schriftlichen
                            Zustimmung des jeweiligen Autors bzw. Erstellers.
                        </p>
                        <p>
                            Downloads und Kopien dieser Seite sind nur für den privaten, nicht kommerziellen
                            Gebrauch gestattet. Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt
                            wurden, werden die Urheberrechte Dritter beachtet. Insbesondere werden Inhalte Dritter
                            als solche gekennzeichnet.
                        </p>
                        <p>
                            Sollten Sie trotzdem auf eine Urheberrechtsverletzung aufmerksam werden, bitten wir um
                            einen entsprechenden Hinweis. Bei Bekanntwerden von Rechtsverletzungen werden wir
                            derartige Inhalte umgehend entfernen.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>Bildnachweise</h2>
                        <p>
                            Die auf dieser Website verwendeten Bilder und Icons stammen aus folgenden Quellen:
                        </p>
                        <ul>
                            <li>Eigene Erstellung</li>
                            <li>Freepik (lizenziert)</li>
                            <li>Unsplash (lizenzfrei)</li>
                            <li>Flaticon (lizenziert)</li>
                        </ul>
                    </div>

                    <div className="legal-section">
                        <h2>Entwicklung und Technik</h2>
                        <p>
                            Diese Plattform wurde entwickelt mit modernen Webtechnologien unter Verwendung von
                            React, Spring Boot und MongoDB. Das Design folgt den Prinzipien der Barrierefreiheit
                            (WCAG 2.1).
                        </p>
                    </div>

                    <div className="legal-notice">
                        <p>
                            <strong>Wichtiger Hinweis:</strong><br />
                            Dies ist ein Muster-Impressum. Bitte passen Sie die Angaben entsprechend Ihren
                            tatsächlichen Gegebenheiten an und lassen Sie diese von einem Rechtsanwalt prüfen.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};
