import React from 'react';
import {Nav, Navbar, NavbarBrand} from 'reactstrap';
import {Link} from 'react-router-dom';

function AppNavbar() {
    return <Navbar color="dark" dark expand="md">
        <Nav> <NavbarBrand tag={Link} to="/">Home</NavbarBrand></Nav>
        <Nav className="justify-content-end w-100"> <NavbarBrand
            href={process.env.REACT_APP_SERVER_ENV + "/logout"}>Logout</NavbarBrand>
        </Nav>
    </Navbar>;
}

export default AppNavbar;