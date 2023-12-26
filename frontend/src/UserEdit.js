import React, {useState} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {useQuery} from "react-query";
import Dropdown from 'react-dropdown';
import Multiselect from 'multiselect-react-dropdown';
import 'react-dropdown/style.css';
import {checkAuth} from "./checkAuth";


const emptyUser = {
    name: '',
    email: '',
    role: '',
    groups: [],
    id: null,
};

function UserEdit() {
    const [user, setUser] = useState(emptyUser);
    const [groups, setGroups] = useState([{id: 1, name: "dsf"}]);
    const [isSelectValidateError, setIsSelectValidateError] = useState(false);
    const {id} = useParams();
    const navigate = useNavigate();
    let userRoles = ["ADMIN", "TEACHER", "STUDENT"];

    const {error, isLoading} = useQuery('users', () =>
            fetch(`/admins-home/users/${id}`, {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                setUser(data);
            },
            enabled: id !== 'new'
        }
    );

    useQuery('groups', () =>
            fetch('/teachers-home/groups', {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                setGroups(data);
            },
        }
    );

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;


    function handleSelectChange(event, name) {
        let newUser = {...user};
        newUser[name] = event.value;
        setUser(newUser);

        if (isSelectValidateError) setIsSelectValidateError(false);
    }

    function onMultiselectChange(selectedList) {
        let newUser = {...user};
        newUser.groups = selectedList;
        setUser(newUser);
    }


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let newUser = {...user};

        newUser[name] = value;
        setUser(newUser);
    }


    async function handleSubmit(event) {
        event.preventDefault();

        await fetch('/admins-home/users/' + (user.id ? "" : 'new'), {
                method: (user.id) ? 'PUT' : 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user),
            }
        );
        navigate("../");
    }

    return <div>
        <Container>
            {<h2>{user.id ? 'Edit user' : 'Add user'}</h2>}
            <Form onSubmit={handleSubmit} className="container-fluid">
                <FormGroup>
                    <Label for="name">Name</Label>
                    <Input required type="text" name="name" id="name" value={user.name || ''}
                           onChange={handleChange} autoComplete="name"/>
                </FormGroup>

                <FormGroup>
                    <Label for="name">Email</Label>
                    <Input required type="text" name="email" id="name" value={user.email || ''}
                           onChange={handleChange} autoComplete="name"/>
                </FormGroup>

                <Multiselect
                    options={groups}
                    selectedValues={user.groups}
                    onSelect={onMultiselectChange}
                    onRemove={onMultiselectChange}
                    displayValue="name"
                />


                <FormGroup style={isSelectValidateError ? {border: '2px solid red'} : {}}>
                    <Label for="role">Role</Label>
                    <Dropdown name="role" id="role" options={userRoles}
                              onChange={(e) => handleSelectChange(e, "role")}
                              value={user.role} placeholder="Select role"/>
                </FormGroup>


                <hr></hr>

                <FormGroup className="my-4">
                    <Button color="primary" type="submit">Save</Button>{' '}
                    <Button color="secondary" tag={Link} to="/">Cancel</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>
}

export default UserEdit;
