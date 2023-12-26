import React, {useState} from 'react';
import {Button, Container, Table} from 'reactstrap';
import {Link} from 'react-router-dom';
import {useQuery} from "react-query";
import {checkAuth} from "./checkAuth"

const EmptyContent = {

    tests: [{
        name: '',
        group: null,
        author: null,
        duration: '',
        endTime: null,
        id: null
    }],
    marks: [{
        value: null,
        id: null,
        testId: null
    }]

};

export default function StudentsHome() {
    const [content, setContent] = useState(EmptyContent);

    const {error, isLoading} = useQuery('tests', () =>
            fetch('students-home/load-tests', {mode: "no-cors"}).then(checkAuth),
        {onSuccess: setContent}
    );

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;

    function testIsAvailable(test) {
        return test.numberOfRetries > content.marks.filter(m => m.testId === test.id).length
            && Date.parse((test.endTime).replace('T', ' ').replace(/-/g, '/')) > Date.now();
    }

    return (
        <div>
            <Container fluid>
                <h3>tests</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="20%">Name</th>
                        <th width="20%">Duration</th>
                        <th width="20%">Deadline</th>
                        <th width="10%">Attempts</th>
                        <th width="20%">Marks</th>
                        <th width="10%">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        content.tests.map(test =>

                            <tr key={test.id}>
                                <td style={{whiteSpace: 'nowrap'}}>{test.name}</td>
                                <td>{test.duration}min</td>
                                <td>{test.endTime}</td>
                                <td>{test.numberOfRetries}</td>

                                <td>
                                    {content.marks.map(mark =>
                                        mark.testId === test.id &&
                                        <span key={mark.id}>{mark.value}, </span>
                                    )
                                    }
                                </td>

                                <td>
                                    {testIsAvailable(test) &&
                                        <Button size="sm" color="primary" tag={Link}
                                                to={`/${test.id}/start`}>start test</Button>
                                    }
                                </td>
                            </tr>
                        )
                    }
                    </tbody>
                </Table>
            </Container>
        </div>
    );
}

