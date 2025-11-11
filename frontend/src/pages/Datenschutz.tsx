export default function Datenschutz() {
    return (
        <div className="legal-page">
            <div className="container">
                <div className="legal-header">
                    <h1 className="page-title">Datenschutzerklärung</h1>
                    <p className="page-subtitle">
                        Schutz Ihrer persönlichen Daten ist uns ein wichtiges Anliegen
                    </p>
                </div>

                <div className="legal-content">
                    <div className="legal-section">
                        <h2>1. Verantwortlicher</h2>
                        <p>
                            Verantwortlich für die Datenverarbeitung auf dieser Website ist:<br />
                            <br />
                            Gesundheitslotse gGmbH<br />
                            Musterstraße 123<br />
                            10115 Berlin<br />
                            Deutschland<br />
                            <br />
                            E-Mail: datenschutz@gesundheitslotse.de<br />
                            Telefon: +49 (0) 800 - GESUND (437863)
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>2. Erhebung und Verarbeitung personenbezogener Daten</h2>

                        <h3>2.1 Bei Besuch unserer Website</h3>
                        <p>
                            Bei jedem Zugriff auf unsere Website erhebt unser System automatisiert Daten und
                            Informationen vom Computersystem des aufrufenden Rechners. Folgende Daten werden hierbei erhoben:
                        </p>
                        <ul>
                            <li>IP-Adresse des anfragenden Rechners</li>
                            <li>Datum und Uhrzeit des Zugriffs</li>
                            <li>Name und URL der abgerufenen Datei</li>
                            <li>Website, von der aus der Zugriff erfolgt (Referrer-URL)</li>
                            <li>Verwendeter Browser und ggf. das Betriebssystem Ihres Rechners</li>
                        </ul>
                        <p>
                            Die Verarbeitung dieser Daten erfolgt zu dem Zweck, die Nutzung der Website zu ermöglichen,
                            die Systemsicherheit und -stabilität zu gewährleisten sowie zu statistischen Zwecken.
                        </p>

                        <h3>2.2 Bei Registrierung und Nutzung unseres Dienstes</h3>
                        <p>
                            Für die Nutzung unserer Gesundheitsberatungs-Plattform ist die Erstellung eines
                            Benutzerkontos erforderlich. Hierbei erheben wir folgende Daten:
                        </p>
                        <ul>
                            <li>Persönliche Daten (Name, Vorname, Geburtsdatum)</li>
                            <li>Kontaktdaten (E-Mail-Adresse, Telefonnummer, Adresse)</li>
                            <li>Gesundheitsbezogene Daten (medizinische Informationen, Symptome)</li>
                            <li>Sprachpräferenzen und Kommunikationseinstellungen</li>
                            <li>Terminvereinbarungen und Beratungsverläufe</li>
                        </ul>
                    </div>

                    <div className="legal-section">
                        <h2>3. Besondere Kategorien personenbezogener Daten</h2>
                        <p>
                            Aufgrund der Art unserer Dienstleistung verarbeiten wir besondere Kategorien
                            personenbezogener Daten im Sinne von Art. 9 DSGVO, insbesondere Gesundheitsdaten.
                        </p>
                        <p>
                            <strong>Rechtsgrundlage:</strong> Die Verarbeitung erfolgt auf Grundlage Ihrer
                            ausdrücklichen Einwilligung gemäß Art. 9 Abs. 2 lit. a DSGVO sowie zur Erbringung
                            von Gesundheitsdienstleistungen gemäß Art. 9 Abs. 2 lit. h DSGVO.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>4. Zwecke der Datenverarbeitung</h2>
                        <p>Wir verarbeiten Ihre personenbezogenen Daten für folgende Zwecke:</p>
                        <ul>
                            <li>Durchführung der Gesundheitsberatung</li>
                            <li>Terminverwaltung und -koordination</li>
                            <li>Kommunikation zwischen Patienten und Beratern</li>
                            <li>Qualitätssicherung und Verbesserung unserer Dienstleistungen</li>
                            <li>Erfüllung gesetzlicher Aufbewahrungspflichten</li>
                            <li>Abwicklung von Zahlungsvorgängen (falls zutreffend)</li>
                        </ul>
                    </div>

                    <div className="legal-section">
                        <h2>5. Datenweitergabe an Dritte</h2>
                        <p>
                            Eine Weitergabe Ihrer personenbezogenen Daten an Dritte erfolgt nur, wenn:
                        </p>
                        <ul>
                            <li>Sie Ihre ausdrückliche Einwilligung erteilt haben</li>
                            <li>die Weitergabe zur Erfüllung vertraglicher Verpflichtungen erforderlich ist</li>
                            <li>eine gesetzliche Verpflichtung zur Weitergabe besteht</li>
                            <li>die Weitergabe zur Geltendmachung, Ausübung oder Verteidigung von Rechtsansprüchen erforderlich ist</li>
                        </ul>

                        <h3>5.1 Dienstleister und Partner</h3>
                        <p>
                            Wir setzen folgende Dienstleister ein, die Zugang zu personenbezogenen Daten erhalten können:
                        </p>
                        <ul>
                            <li>IT-Dienstleister für Hosting und Wartung</li>
                            <li>Zahlungsdienstleister</li>
                            <li>Versanddienstleister für Kommunikation</li>
                            <li>Qualifizierte Gesundheitsberater und Experten</li>
                        </ul>
                        <p>
                            Alle Dienstleister sind vertraglich zur Einhaltung der datenschutzrechtlichen Bestimmungen verpflichtet.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>6. Datensicherheit</h2>
                        <p>
                            Wir treffen umfangreiche technische und organisatorische Sicherheitsmaßnahmen,
                            um Ihre Daten vor Verlust, Manipulation und unberechtigtem Zugriff zu schützen:
                        </p>
                        <ul>
                            <li>Verschlüsselung aller Datenübertragungen (SSL/TLS)</li>
                            <li>Verschlüsselung gespeicherter Gesundheitsdaten</li>
                            <li>Zugriffsbeschränkungen und Berechtigungskonzepte</li>
                            <li>Regelmäßige Sicherheitsüberprüfungen und Audits</li>
                            <li>Ausfallsichere Infrastruktur mit Redundanzen</li>
                        </ul>
                    </div>

                    <div className="legal-section">
                        <h2>7. Speicherdauer</h2>
                        <p>
                            Wir speichern Ihre personenbezogenen Daten nur so lange, wie dies für die
                            Erfüllung der Verarbeitungszwecke erforderlich ist oder gesetzliche
                            Aufbewahrungsfristen bestehen.
                        </p>
                        <p>
                            <strong>Gesundheitsdaten:</strong> Gemäß ärztlicher und therapeutischer
                            Dokumentationspflichten werden Gesundheitsdaten für mindestens 10 Jahre aufbewahrt.
                        </p>
                        <p>
                            <strong>Vertragsdaten:</strong> Vertragsrelevante Daten werden für die Dauer
                            der gesetzlichen Aufbewahrungsfristen von 6 bis 10 Jahren gespeichert.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>8. Ihre Rechte</h2>
                        <p>Sie haben folgende Rechte bezüglich Ihrer personenbezogenen Daten:</p>

                        <h3>8.1 Auskunftsrecht</h3>
                        <p>
                            Sie haben das Recht, eine Bestätigung darüber zu verlangen, ob wir personenbezogene
                            Daten von Ihnen verarbeiten und gegebenenfalls Auskunft über diese Daten zu erhalten.
                        </p>

                        <h3>8.2 Recht auf Berichtigung</h3>
                        <p>
                            Sie haben das Recht, die Berichtigung unrichtiger oder Vervollständigung
                            unvollständiger personenbezogener Daten zu verlangen.
                        </p>

                        <h3>8.3 Recht auf Löschung</h3>
                        <p>
                            Sie haben das Recht, die Löschung Ihrer personenbezogenen Daten zu verlangen,
                            sofern keine gesetzlichen Aufbewahrungspflichten entgegenstehen.
                        </p>

                        <h3>8.4 Recht auf Einschränkung der Verarbeitung</h3>
                        <p>
                            Sie haben das Recht, die Einschränkung der Verarbeitung Ihrer personenbezogenen
                            Daten zu verlangen.
                        </p>

                        <h3>8.5 Recht auf Datenübertragbarkeit</h3>
                        <p>
                            Sie haben das Recht, die Sie betreffenden personenbezogenen Daten in einem
                            strukturierten, gängigen und maschinenlesbaren Format zu erhalten.
                        </p>

                        <h3>8.6 Widerspruchsrecht</h3>
                        <p>
                            Sie haben das Recht, aus Gründen, die sich aus Ihrer besonderen Situation ergeben,
                            jederzeit gegen die Verarbeitung Sie betreffender personenbezogener Daten Widerspruch
                            einzulegen.
                        </p>

                        <h3>8.7 Widerrufsrecht bei Einwilligungen</h3>
                        <p>
                            Sie haben das Recht, erteilte Einwilligungen jederzeit mit Wirkung für die
                            Zukunft zu widerrufen.
                        </p>

                        <h3>8.8 Beschwerderecht</h3>
                        <p>
                            Sie haben das Recht, sich bei einer Aufsichtsbehörde zu beschweren, wenn Sie der
                            Ansicht sind, dass die Verarbeitung Ihrer personenbezogenen Daten gegen die DSGVO verstößt.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>9. Cookies und Tracking-Technologien</h2>
                        <p>
                            Wir verwenden Cookies und ähnliche Technologien, um die Funktionalität unserer
                            Website zu gewährleisten und die Nutzererfahrung zu verbessern.
                        </p>

                        <h3>9.1 Notwendige Cookies</h3>
                        <p>
                            Diese Cookies sind für den Betrieb der Website erforderlich und können nicht
                            deaktiviert werden:
                        </p>
                        <ul>
                            <li>Session-Cookies für die Anmeldeverwaltung</li>
                            <li>Sicherheits-Cookies für den Schutz vor Missbrauch</li>
                            <li>Lastverteilungs-Cookies für die Performance</li>
                        </ul>

                        <h3>9.2 Analyse-Cookies</h3>
                        <p>
                            Mit Ihrer Einwilligung verwenden wir Analyse-Cookies, um die Nutzung unserer
                            Website zu verstehen und zu verbessern.
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>10. Kontakt zum Datenschutzbeauftragten</h2>
                        <p>
                            Unser betrieblicher Datenschutzbeauftragter steht Ihnen für alle Fragen zum
                            Datenschutz gerne zur Verfügung:
                        </p>
                        <p>
                            <strong>Datenschutzbeauftragter</strong><br />
                            Max Datenschutz<br />
                            E-Mail: datenschutz@gesundheitslotse.de<br />
                            Telefon: +49 (0) 30 - 123 456 78
                        </p>
                    </div>

                    <div className="legal-section">
                        <h2>11. Änderungen dieser Datenschutzerklärung</h2>
                        <p>
                            Wir behalten uns vor, diese Datenschutzerklärung anzupassen, damit sie stets
                            den aktuellen rechtlichen Anforderungen entspricht oder um Änderungen unserer
                            Leistungen in der Datenschutzerklärung umzusetzen.
                        </p>
                        <p>
                            Die jeweils aktuelle Version ist auf unserer Website abrufbar.
                        </p>
                    </div>

                    <div className="legal-notice">
                        <p>
                            <strong>Stand:</strong> Januar 2024<br />
                            <strong>Letzte Aktualisierung:</strong> 15. Januar 2024
                        </p>
                        <p>
                            <strong>Wichtiger Hinweis:</strong><br />
                            Diese Datenschutzerklärung ist ein Muster und muss an Ihre spezifischen
                            Verarbeitungstätigkeiten angepasst werden. Bitte lassen Sie diese von einem
                            Datenschutzexperten oder Rechtsanwalt prüfen.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};
