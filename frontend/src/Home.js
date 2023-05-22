import React from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import {Link} from 'react-router-dom';
import {Button, Container} from 'reactstrap';

function Home() {

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <Button color="link"><Link to="students-home">student's personal page</Link></Button>
                <Button color="link"><Link to="teachers-home">teacher's personal page</Link></Button>
            </Container>
        </div>
    );
}

export default Home;