import React, {useState} from 'react';
import {Button, Container, Table} from 'reactstrap';
import {Link, useParams} from 'react-router-dom';
import {useQuery} from "react-query";
import {checkAuth} from "./checkAuth"

const EmptyResults = {
    users: [{
        name: '', id: 0,

    }],

    marks: [{
        questionText: "", id: 1, userId: 0
    }],
};

export default function TestResults() {
    const [results, setResults] = useState(EmptyResults);
    const {id} = useParams();

    const {error, isLoading} = useQuery('testResults', () =>
            fetch(`/teachers-home/tests/${id}/results`,
                {mode: "no-cors"}).then(checkAuth),
        {onSuccess: setResults});

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;

    return (<div>
        <Container fluid>
            <h3>Test Results</h3>
            <Table className="mt-4">
                <thead>
                <tr>
                    <th width="30%">Name</th>
                    <th width="40%">Mark</th>
                </tr>
                </thead>
                <tbody>
                {results.users.map(user => <tr key={user.id}>
                    <td style={{whiteSpace: 'nowrap'}}>{user.name}</td>
                    <td>
                        {results.marks.filter(mark => mark.userId === user.id).map(mark => <span
                            key={mark.id}>{mark.value + ", "}</span>)}

                    </td>
                </tr>)}
                </tbody>
            </Table>
            <div className="float-right">
                <Button color="secondary" tag={Link} to="../../">cancel</Button>
            </div>
        </Container>
    </div>);
}