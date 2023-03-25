import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class TestList extends Component {

    constructor(props) {
        super(props);
        this.state = {tests: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('http://localhost:8080/subload/teachersHome')
            .then(response => response.json())
            .then(data => this.setState({tests: data}));
    }





    async remove(id) {
        await fetch(`http://localhost:8080/subload/tests/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedTests = [...this.state.tests].filter(i => i.id !== id);
            this.setState({tests: updatedTests});
        });
    }

    render() {
        const {tests, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const testList = tests.map(test => {
            return <tr key={test.id}>
                <td style={{whiteSpace: 'nowrap'}}>{test.name}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/tests/" + test.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(test.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <h3>tests</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Name</th>
                            <th width="30%">Email</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {testList}
                        </tbody>
                    </Table>

                    <div className="float-right">
                        <Button color="success" tag={Link} to="/tests/new">Add test</Button>
                    </div>
                </Container>
            </div>
        );
    }


















}
export default TestList;