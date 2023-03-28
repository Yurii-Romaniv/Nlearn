
import React, { Component} from 'react';
import  { useState } from 'react';
import { Link, withRouter } from 'react-router-dom';
import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AppNavbar from './AppNavbar';

class TestEdit extends Component {

    maxId=1;



    handleChange(event) {

        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};


        if(name.includes(';') ) {
            const indexes =name.split(";");

            if (indexes[1]) {
                if (target.type === "checkbox") {
                    let q =item.questions.find(q => q.id == indexes[0]);
                    if(!target.checked){
                        (q.answers) = (q.answers).filter(item => item !== +indexes[1] );
                    }else {
                        if(!q.answers.includes(+indexes[1])){
                            (q.answers).push(+indexes[1]);
                        }

                    }

                } else {
                    ((item.questions[indexes[0]]).answerVariants)[indexes[1]] = value;
                }
            } else {
                (item.questions[indexes[0]]).question = value;
            }
        }else{
            item.test[name] = value;

            if(name=="group"){
                if(/(([a-z]{1,})|([A-Z]{1,}))-[1-9]{2}/.test(value)) {
                    fetch('http://localhost:8080/subload/check_group/'+value)
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


                    }
                }else{
                    target.style.backgroundColor = "red";
            }

        }


        this.setState({item});
    }



    async handleSubmit(event) {
        event.preventDefault();
        var {item} = this.state;
        item.added= Array.from(item.added);
        item.deleted= Array.from(item.deleted);
        await fetch('http://localhost:8080/subload/tests' + (item.test.id ? '/' + item.test.id : ''), {
            method: (item.test.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
    }










    async delAnswer(index, sIndex) {
        let item = {...this.state.item};
        let q =item.questions[index]
        q.answerVariants=  (q.answerVariants).filter(i => i !== q.answerVariants[sIndex]);
        this.setState({item});

        q.answers = (q.answers).filter(item => item !== +sIndex);

        q.answers.forEach(function(a,index, thisArr) {
            if(a > +sIndex){
                thisArr[index]-=1;
            }
        });

    }


    async addAnswer(index) {
        let item = {...this.state.item};
        (item.questions[index]).answerVariants.push([]);
        this.setState({item});
    }



    async addQuestion() {
        let item = {...this.state.item};

        const que= {name:"", id:++(this.maxId), answerVariants:[""],answers:[]};
        item.questions.push(que);
        item.added.add(que.id)
        this.setState({item});
    }






    isChecked(id, sIndex){
        let item = {...this.state.item};//
        if((item.questions.find(q => q.id == id)).answers.includes(sIndex)){
            return true;
        }
        return false;

    }






    async delQuestion(index) {
        let item = {...this.state.item};
        let questionId=item.questions[index].id

        if(item.added.has(questionId)){
            item.added.delete(questionId);
        }else{
            item.deleted.add(questionId);
        }


        item.questions=  item.questions.filter(i => i !== item.questions[index]);
        this.setState({item});
    }


    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const newItem = (await (await fetch(`http://localhost:8080/subload/tests/${this.props.match.params.id}`)).json());

            //console.log(this.maxId);
            let pThis = this;
            newItem.questions.forEach(function(q) {
                if(q.id > pThis.maxId){
                    pThis.maxId=q.id;
                }
            });

            newItem.added=new Set();
            newItem.deleted=new Set();
            newItem.test.group= newItem.test.group.name;
            this.setState({item: newItem});
        }
    }

    emptyItem = {
        test:{
            name: '',
            group: '',
            duration: '',
            id: ''
        },

        questions: [{
            answerVariants:[""],
            answers:[]

        } ],

        added:new Set(),
        deleted:new Set()

    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    render() {
        const {item} = this.state;
        const forms= item.questions;
        const title = <h2>{item.test.id ? 'Edit Test' : 'Add Test'}</h2>;


        const formList = forms.map((form, index) => {
            return <FormGroup key={form.id} className="my-4" style={{backgroundColor:"lightblue"}}>


                <Input className="col-9 bold" placeholder="name of question" type="text" name={index + ";"} value={form.question|| ''}  onChange={this.handleChange}  style={{fontWeight:"bold"} }/>


                {form.answerVariants.map((answer, sIndex) => {
                    return <div className="d-flex justify-content-center container my-auto col-11">

                        <Input className="col-1" type="checkbox" name={form.id+ ";" +sIndex}  checked={this.isChecked(form.id, sIndex)}  value={this.isChecked(form.id, sIndex)} onChange={this.handleChange}/>

                        <Input className="col-9" type="text" name={index+ ";" +sIndex} value={answer|| ''}
                               onChange={this.handleChange}/>
                        <Button className="col-1" size="sm" color="danger" onClick={() => this.delAnswer(index, sIndex)}>Delete</Button>
                    </div>

                })
                }

                <Button className="col-1" size="sm"  color="primary" onClick={() => this.addAnswer(index)}>add one</Button>
                <Button className="col-2" size="sm"  color="danger" onClick={() => this.delQuestion(index)}>delete question</Button>

                <hr></hr>


            </FormGroup>
        });







        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit} className="container-fluid">
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.test.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>

                    <FormGroup>
                        <Label for="group">For group</Label>
                        <Input type="text" name="group" id="group"  value={item.test.group || ''}
                               onChange={this.handleChange} />
                    </FormGroup>

                    <FormGroup>
                        <Label for="duration">passing time(minutes)</Label>
                        <Input type="number" name="duration" id="duration" value={item.test.duration || ''}
                               onChange={this.handleChange} autoComplete="duration"/>
                    </FormGroup>

                    <hr></hr>
                    {formList}

                    <Button className="my-4" color="warning" onClick={() => this.addQuestion()} >one more question</Button>

                    <FormGroup className="my-4">
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/teachersHome">Cancel</Button>
                    </FormGroup>

                </Form>


            </Container>
        </div>
    }






}
export default TestEdit;



