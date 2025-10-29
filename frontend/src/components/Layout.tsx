import React, {type ReactNode } from 'react';
import Navigation from './Navigation';
import './Layout.css';
import Footer from "./Footer.tsx";

interface LayoutProps {
    children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
    return (
        <div className="layout">
            <Navigation />
            <main className="main-content">
                {children}
            </main>
            <Footer />
        </div>
    );
};

export default Layout;