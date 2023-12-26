import React, {useState} from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {useQuery} from "react-query";
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import {checkAuth} from "./checkAuth";
import {Test} from "./entities/Test"
import {Question} from "./entities/Question"
import {Answer} from "./entities/Answer"

function TestEdit() {
    const [test, setTest] = useState(new Test());
    console.log(new Test());
    const [groups, setGroups] = useState([{id: 1, name: ""}]);
    const [isGroupValidateError, setIsGroupValidateError] = useState(false);
    const {id} = useParams();

    const {error, isLoading} = useQuery('fullTests', () =>
            fetch(`/teachers-home/tests/${id}`, {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                setTest(data);
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


    function handleGroupChange(event) {
        let newTest = {...test};
        console.log(event.value)
        console.log(newTest.group)
        newTest.group = event.value;
        setTest(newTest);
        if (isGroupValidateError) setIsGroupValidateError(false);
    }


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let newTest = {...test};

        newTest[name] = value;
        setTest(newTest);
    }


    function handleCheckboxChange(index, sIndex, event) {
        let newTest = {...test};
        const target = event.target;
        newTest.questions[index].answerVariants[sIndex].isCorrect = target.checked;
        setTest(newTest);
    }

    function handleQuestionsChange(id, sId = null, event) {
        const value = event.target.value;
        let newTest = {...test};

        sId === null
            ? newTest.questions[id].questionText = value
            : (newTest.questions[id]).answerVariants[sId].answerText = value;

        setTest(newTest);
    }


    async function handleSubmit(event) {
        event.preventDefault();

        if (test.group.name) {
            await fetch('/teachers-home/tests/' + (test.id ?? 'new'), {
                method: (test.id) ? 'PUT' : 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(test),
            });
        } else {
            setIsGroupValidateError(true);
        }
    }


    async function delAnswer(index, sIndex) {
        let newTest = {...test};
        let question = newTest.questions[index];
        question.answerVariants.splice(sIndex, 1)
        setTest(newTest);
    }


    async function addAnswer(index) {
        let newTest = {...test};
        (newTest.questions[index]).answerVariants.push(new Answer());
        setTest(newTest);
    }

    function addQuestion() {
        let newTest = {...test};
        newTest.questions.push(new Question());
        setTest(newTest);
    }

    async function delQuestion(index) {
        let newTest = {...test};
        newTest.questions.splice(index, 1);
        setTest(newTest);
    }


    let options = [];
    groups.forEach(function (g) {
        options.push({value: g, label: g.name})
    });

    return <div>
        <Container>
            {<h2>{test.id ? 'Edit Test' : 'Add Test'}</h2>}
            <Form onSubmit={handleSubmit} className="container-fluid">
                <FormGroup>
                    <Label for="name">Name</Label>
                    <Input required type="text" name="name" id="name" value={test.name || ''}
                           onChange={handleChange} autoComplete="name"/>
                </FormGroup>

                <FormGroup style={isGroupValidateError ? {border: '2px solid red'} : {}}>
                    <Label for="group">For group</Label>
                    <Dropdown name="group" id="group" options={options} onChange={handleGroupChange}
                              value={test.group.name} placeholder="Select group"/>
                </FormGroup>

                <FormGroup>
                    <Label for="duration">passing time(minutes)</Label>
                    <Input required type="number" name="duration" id="duration" value={test.duration || ''}
                           onChange={handleChange}/>
                </FormGroup>

                <FormGroup>
                    <Label for="numberOfRetries">number of retries</Label>
                    <Input required type="number" name="numberOfRetries" id="numberOfRetries"
                           value={test.numberOfRetries || ''}
                           onChange={handleChange}/>
                </FormGroup>

                <FormGroup>
                    <Label for="endTime">end time</Label>
                    <Input required type="datetime-local" name="endTime" id="endTime" value={test.endTime || ''}
                           onChange={handleChange}/>
                </FormGroup>

                <hr></hr>
                {
                    test.questions.map((question, index) =>
                        <FormGroup key={index} className="my-4" style={{backgroundColor: "lightblue"}}>

                            <Input required className="col-9 bold" placeholder="name of question" type="text"
                                   name={index + ";"}
                                   value={question.questionText || ''}
                                   onChange={(e) => handleQuestionsChange(index, null, e)}
                                   style={{fontWeight: "bold"}}/>

                            {
                                question.answerVariants.map((answer, sIndex) =>
                                    <div key={sIndex}
                                         className="d-flex justify-content-center container my-auto col-11">
                                        <Input className="col-1" type="checkbox" name={question.id + ";" + sIndex}
                                               checked={answer.isCorrect} value={answer.isCorrect}
                                               onChange={(e) => handleCheckboxChange(index, sIndex, e)}/>
                                        <Input required className="col-9" type="text" name={index + ";" + sIndex}
                                               value={answer.answerText || ''}
                                               onChange={(e) => handleQuestionsChange(index, sIndex, e)}/>
                                        <Button className="col-1" size="sm" color="danger"
                                                onClick={() => delAnswer(index, sIndex)}>Delete</Button>
                                    </div>
                                )
                            }

                            <Button className="col-1" size="sm" color="primary" onClick={() => addAnswer(index)}>add
                                one</Button>
                            <Button className="col-2" size="sm" color="danger" onClick={() => delQuestion(index)}>delete
                                question</Button>
                            <hr></hr>
                        </FormGroup>
                    )
                }

                <Button className="my-4" color="warning" onClick={() => addQuestion()}>one more question</Button>

                <FormGroup className="my-4">
                    <Button color="primary" type="submit">Save</Button>{' '}
                    <Button color="secondary" tag={Link} to="/">Cancel</Button>
                </FormGroup>
            </Form>
        </Container>
    </div>
}

export default TestEdit;
