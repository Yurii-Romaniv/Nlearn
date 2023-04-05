import React, { useEffect} from 'react';
import  { useState } from 'react';
import {Link, useParams} from 'react-router-dom';
import {Button,  Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AppNavbar from './AppNavbar';



const emptyItem = {
    test:{
        name: '',
        group:null,
        author:null,
        duration: '',
        id: null
    },

    questions: [{
        question:"",
        id:1,
        answerVariants:[""],
        answers:[]

    } ],

    added:new Set(),
    deleted:new Set(),
    groupName:''

};

function TestEdit(){
    const [item, setItem] = useState(emptyItem);
    var maxId=1;
    const { id } = useParams();


    function handleGroupChange(event) {
        const target = event.target;
        const value = target.value;
        let newItem = {...item};


        if(/(([a-z]{1,})|([A-Z]{1,}))-[1-9]{2}/.test(value)) {
            fetch('/subload/check_group/'+value)
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Failed to fetch boolean value');
                    }
                })
                .then(data => {
                    if (data) {
                        target.style.backgroundColor = "green";
                    } else {
                        target.style.backgroundColor = "red";}
                })
                .catch(error => {
                    console.error(error);
                });

            }else{
                target.style.backgroundColor = "red";
            }

        newItem.groupName = value;
        setItem(newItem);
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

        let q =newItem.questions.find(q => q.id === +id);
        if(!target.checked){
            (q.answers) = (q.answers).filter(item => item !== +sId );
        }else {
            if(!q.answers.includes(+sId)){
                (q.answers).push(+sId);
            }
        }
            setItem(newItem);
        }

    function handleQuestionsChange( id,  sId =null,event ) {
        const value =  event.target.value;
        let newItem = {...item};

        if(sId===null){
            newItem.questions[id].question = value;
        }else {
            (newItem.questions[id]).answerVariants[sId] = value;
        }
        setItem(newItem);
    }


    async function handleSubmit(event) {
        event.preventDefault();
        item.added= Array.from(item.added);
        item.deleted= Array.from(item.deleted);

        await fetch('/subload/tests' + (item.test.id ? '/' + item.test.id : ''), {
            method: (item.test.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
    }


    async function delAnswer(index, sIndex) {
        let newItem = {...item};
        let q =newItem.questions[index]

        q.answerVariants=  (q.answerVariants).filter(i => i !== q.answerVariants[sIndex]);
        setItem(newItem);
        q.answers = (q.answers).filter(a => a !== +sIndex);
        q.answers.forEach(function(a,index, thisArr) {
            if(a > +sIndex){
                thisArr[index]-=1;
            }
        });

    }


    async function addAnswer(index) {
        let newItem = {...item};

        (newItem.questions[index]).answerVariants.push([]);
        setItem(newItem);
    }


    async function addQuestion() {
        let newItem = {...item};
        const que= {name:"", id:+(++(maxId)), answerVariants:[""],answers:[]};

        newItem.questions.push(que);
        newItem.added.add(que.id)
        setItem(newItem);
    }


    function isChecked(id, sIndex){
        return (item.questions.find(q => q.id === +id)).answers.includes(sIndex);
    }


    async function delQuestion(index) {
        let newItem = {...item};
        let questionId=newItem.questions[index].id

        if(newItem.added.has(questionId)){
            newItem.added.delete(questionId);
        }else{
            newItem.deleted.add(questionId);
        }
        newItem.questions=  newItem.questions.filter(i => i !== newItem.questions[index]);
        setItem(newItem);
    }


    useEffect(() => {
        const dataFetch = async () => {
            const data = await (
                await fetch(`/subload/tests/${id}`)
            ).json();

            data.questions.forEach(function (q) {
                if (q.id > maxId) {
                    maxId = q.id;
                }
            });

            data.added = new Set();
            data.deleted = new Set();
            data.groupName = data.test.group.name;
            setItem(data);
        };

        if (id !== 'new') {
            dataFetch();
        }
    }, []);


        return <div>
            <AppNavbar/>
            <Container>
                {<h2>{item.test.id ? 'Edit Test' : 'Add Test'}</h2>}
                <Form onSubmit={handleSubmit} className="container-fluid">
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.test.name || ''}
                               onChange={handleChange} autoComplete="name"/>
                    </FormGroup>

                    <FormGroup>
                        <Label for="group">For group</Label>
                        <Input type="text" name="group" id="group"  value={item.groupName || ''}
                               onChange={handleGroupChange}/>
                    </FormGroup>

                    <FormGroup>
                        <Label for="duration">passing time(minutes)</Label>
                        <Input type="number" name="duration" id="duration" value={item.test.duration || ''}
                               onChange={handleChange} autoComplete="duration"/>
                    </FormGroup>

                    <hr></hr>
                    {
                        item.questions.map((form, index) => {
                            return <FormGroup key={form.id} className="my-4" style={{backgroundColor:"lightblue"}}>

                            <Input className="col-9 bold" placeholder="name of question" type="text" name={index + ";"} value={form.question|| ''}  onChange={(e)=>handleQuestionsChange(index,null ,e)} style={{fontWeight:"bold"} }/>

                            {
                                form.answerVariants.map((answer, sIndex) => {
                                    return <div className="d-flex justify-content-center container my-auto col-11">
                                    <Input className="col-1" type="checkbox" name={form.id+ ";" +sIndex}  checked={isChecked(form.id, sIndex)}  value={isChecked(form.id, sIndex)}
                                    onChange={(e)=>handleCheckboxChange(form.id,sIndex,e)}/>
                                    <Input className="col-9" type="text" name={index+ ";" +sIndex} value={answer|| ''}
                                    onChange={(e)=>handleQuestionsChange(index,sIndex,e)}/>
                                    <Button className="col-1" size="sm" color="danger" onClick={() => delAnswer(index, sIndex)}>Delete</Button>
                                    </div>
                                    })
                            }

                            <Button className="col-1" size="sm"  color="primary" onClick={() => addAnswer(index)}>add one</Button>
                            <Button className="col-2" size="sm"  color="danger" onClick={() => delQuestion(index)}>delete question</Button>
                            <hr></hr>
                            </FormGroup>
                        })
                    }

                    <Button className="my-4" color="warning" onClick={() => addQuestion()} >one more question</Button>

                    <FormGroup className="my-4">
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/teachersHome">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
}
export default TestEdit;
