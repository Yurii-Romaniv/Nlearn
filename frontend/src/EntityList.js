import React, {useState} from 'react';
import {Button, ButtonGroup, Container, Table} from 'reactstrap';
import {Link} from 'react-router-dom';
import {useQuery} from "react-query";
import {checkAuth} from "./checkAuth"

const EmptyEntity = [{
    name: '',
    id: null,
    email: '',
}];

export default function EntityList(props) {
    const fetchUrl = props.fetchUrl;
    const entityName = props.entityName;

    const [entities, setEntities] = useState(EmptyEntity);
    const [loadAll, setLoadAll] = useState(false);
    const {error, isLoading} = useQuery([entityName, loadAll], () =>
            fetch(fetchUrl + "load" + (loadAll ? "-all" : ""), {mode: "no-cors"}).then(checkAuth),
        {onSuccess: setEntities}
    );

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;


    async function remove(id) {
        await fetch(fetchUrl + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedEntities = entities.filter(i => i.id !== id);
            setEntities(updatedEntities);
        })
    }

    return (
        <div>
            <Container fluid>
                <h3>{entityName}s</h3>
                <Table className="mt-4">
                    <thead>
                    <tr>
                        <th width="30%">Name</th>
                        {entityName === "user" && <th width="30%">Email</th>}
                        <th width="40%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        entities.map(entity =>
                            <tr key={entity.id}>
                                <td style={{whiteSpace: 'nowrap'}}>{entity.name}</td>
                                {entityName === "user" && <td style={{whiteSpace: 'nowrap'}}>{entity.email}</td>}
                                <td>
                                    <ButtonGroup>
                                        <Button size="sm" color="primary" tag={Link}
                                                to={entityName + "s/" + entity.id}>Edit</Button>
                                        <Button size="sm" color="danger" onClick={() => remove(entity.id)}>Delete</Button>
                                        {entityName === "test" && <Button size="sm" color="primary" tag={Link} to={"/tests/" + entity.id + "/results"}>view results</Button>}
                                    </ButtonGroup>
                                </td>
                            </tr>
                        )
                    }
                    </tbody>
                </Table>
                <div className="float-right">
                    <Button color="success" tag={Link} to={entityName + "s/new"}>Add {entityName}</Button>
                    <Button color="primary" onClick={() => setLoadAll(loadAll => !loadAll)}>
                        {loadAll ? "roll up" : "Load all " + entityName}
                    </Button>
                </div>
            </Container>
        </div>
    );
}

