import React, {useState} from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import {useQuery} from "react-query";
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import {checkAuth} from "./checkAuth";


const emptyItem = {
    test: {
        name: '',
        group: {
            name: null
        },
        author: null,
        duration: '',
        id: null,
        numberOfRetries: 1,
        endTime: null
    },

    questions: [{
        questionText: "",
        id: 1,
        answerVariants: [""],
        correctIndexes: [],
        numberOfCorrectAnswers: 0

    }],

    addedIds: new Set(),
    deletedIds: new Set(),
    groups: []

};

function TestEdit() {
    const [item, setItem] = useState(emptyItem);
    const [maxId, setMaxId] = useState(0);
    const [isGroupValidateError, setIsGroupValidateError] = useState(false);
    const {id} = useParams();
    const navigate = useNavigate();

    const {error, isLoading} = useQuery('fullTests', () =>
            fetch(`/teachers-home/tests/${id}`, {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                setMaxId(Math.max(...data.questions.map(q => q.id))+1);
                data.addedIds = new Set();
                data.deletedIds = new Set();
                data.groupName = data.test.group.name;
                setItem(data);
            },
            enabled: id !== 'new'
        }
    );

    useQuery('groups', () =>
            fetch('/teachers-home/groups', {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                let newItem = emptyItem;
                newItem.groups = data;
                setItem(newItem);
            },
            enabled: id === 'new'
        }
    );

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;


    function handleGroupChange(event) {
        let newItem = {...item};
        newItem.test.group = event.value;
        setItem(newItem);
        if (isGroupValidateError) setIsGroupValidateError(false);
    }


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let newItem = {...item};

        newItem.test[name] = value;
        setItem(newItem);
    }


    function handleCheckboxChange(id, sId, event) {
        let newItem = {...item};
        const target = event.target;

        let question = newItem.questions.find(q => q.id === +id);
        if (!target.checked) {
            (question.correctIndexes) = (question.correctIndexes).filter(item => item !== +sId);
            question.numberOfCorrectAnswers--;
        } else {
            if (!question.correctIndexes.includes(+sId)) {
                (question.correctIndexes).push(+sId);
                question.numberOfCorrectAnswers++;
            }
        }
        setItem(newItem);
    }

    function handleQuestionsChange(id, sId = null, event) {
        const value = event.target.value;
        let newItem = {...item};

        sId === null
            ? newItem.questions[id].questionText = value
            : (newItem.questions[id]).answerVariants[sId] = value;

        setItem(newItem);
    }


    async function handleSubmit(event) {
        event.preventDefault();
        item.addedIds = Array.from(item.addedIds);
        item.deletedIds = Array.from(item.deletedIds);

        if (item.test.group.name) {
            await fetch('/teachers-home/tests/' + (item.test.id ?? 'new'), {
                method: (item.test.id) ? 'PUT' : 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(item),
            });
            navigate("../");
        } else {
            setIsGroupValidateError(true);
        }
    }


    async function delAnswer(index, sIndex) {
        let newItem = {...item};
        let question = newItem.questions[index];

        question.answerVariants = (question.answerVariants).filter(i => i !== question.answerVariants[sIndex]);
        setItem(newItem);
        question.correctIndexes = (question.correctIndexes).filter(a => a !== +sIndex);
        question.numberOfCorrectAnswers= question.correctIndexes.length;
        question.correctIndexes.forEach(function (a, index, thisArr) {
            if (a > +sIndex) {
                thisArr[index]--;
            }
        });

    }


    async function addAnswer(index) {
        let newItem = {...item};

        (newItem.questions[index]).answerVariants.push([]);
        setItem(newItem);
    }


    function addQuestion() {
        let newItem = {...item};
        setMaxId(maxId+1);
        const emptyQuestion = {questionText: "", id: maxId, answerVariants: [""], correctIndexes: [], numberOfCorrectAnswers:0};
        newItem.questions.push(emptyQuestion);
        newItem.addedIds.add(emptyQuestion.id);
        setItem(newItem);
    }


    function isChecked(id, sIndex) {
        return (item.questions.find(q => q.id === +id)).correctIndexes.includes(sIndex);
    }


    async function delQuestion(index) {
        let newItem = {...item};
        let questionId = newItem.questions[index].id;

        newItem.addedIds.has(questionId)
            ? newItem.addedIds.delete(questionId)
            : newItem.deletedIds.add(questionId);

        newItem.questions = newItem.questions.filter(i => i !== newItem.questions[index]);
        setItem(newItem);
    }

    let options = [];
    item.groups.forEach(function (g) {
        options.push({value: g, label: g.name})
    });


    return <div>
        <Container>
            {<h2>{item.test.id ? 'Edit Test' : 'Add Test'}</h2>}
            <Form onSubmit={handleSubmit} className="container-fluid">
                <FormGroup>
                    <Label for="name">Name</Label>
                    <Input required type="text" name="name" id="name" value={item.test.name || ''}
                           onChange={handleChange} autoComplete="name"/>
                </FormGroup>

                <FormGroup style={isGroupValidateError? {border: '2px solid red'}: {}}>
                    <Label for="group">For group</Label>
                    <Dropdown name="group" id="group" options={options} onChange={handleGroupChange}
                              value={item.test.group.name} placeholder="Select group"/>

                </FormGroup>

                <FormGroup>
                    <Label for="duration">passing time(minutes)</Label>
                    <Input required type="number" name="duration" id="duration" value={item.test.duration || ''}
                           onChange={handleChange}/>
                </FormGroup>

                <FormGroup>
                    <Label for="numberOfRetries">number of retries</Label>
                    <Input required type="number" name="numberOfRetries" id="numberOfRetries" value={item.test.numberOfRetries || ''}
                           onChange={handleChange}/>
                </FormGroup>

                <FormGroup>
                    <Label for="endTime">end time</Label>
                    <Input required type="datetime-local" name="endTime" id="endTime" value={item.test.endTime || ''}
                           onChange={handleChange}/>
                </FormGroup>

                <hr></hr>
                {
                    item.questions.map((form, index) =>
                        <FormGroup key={form.id} className="my-4" style={{backgroundColor: "lightblue"}}>

                            <Input required className="col-9 bold" placeholder="name of question" type="text" name={index + ";"}
                                   value={form.questionText || ''}
                                   onChange={(e) => handleQuestionsChange(index, null, e)}
                                   style={{fontWeight: "bold"}}/>

                            {
                                form.answerVariants.map((answer, sIndex) =>
                                    <div key={sIndex.id}
                                         className="d-flex justify-content-center container my-auto col-11">
                                        <Input className="col-1" type="checkbox" name={form.id + ";" + sIndex}
                                               checked={isChecked(form.id, sIndex)} value={isChecked(form.id, sIndex)}
                                               onChange={(e) => handleCheckboxChange(form.id, sIndex, e)}/>
                                        <Input required className="col-9" type="text" name={index + ";" + sIndex}
                                               value={answer || ''}
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
