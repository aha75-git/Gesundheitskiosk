import './Footer.css'
import {Link} from "react-router-dom";

export default function Footer() {
    const currentYear = new Date().getFullYear()
    return (
        <footer>
            <p>
                &copy; {currentYear} Gesundheitskiosk. Alle Rechte vorbehalten.
                <br />
                <Link to="/impressum">Impressum</Link> / <Link to="/datenschutz">Datenschutz</Link>
            </p>
        </footer>
    )
}