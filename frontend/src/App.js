import './App.css';
import {Component} from "react";
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import TestEdit from "./TestEdit";
import FormList from "./FormList";
import 'bootstrap/dist/css/bootstrap.css';
//


//            {/<Route path='/clients/:id' component={ClientEdit}/>}
class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/teachersHome' exact={true} component={FormList}/>
                    <Route path='/tests/:id' component={TestEdit}/>
                </Switch>
            </Router>
        )
    }
}

export default App;




/*
import logo from './logo.svg';

import './App.css';
import {Component} from "react";

class App extends Component {
  state = {
    forms: []
  };

  async componentDidMount() {
    const response = await fetch('http://localhost:8080/subload/t');
    const body = await response.json();
    console.log(body);
    this.setState({forms: body});
  }

  render() {
    const {forms} = this.state;
    return (
        <div className="App">
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <div className="App-intro">
              <h2>forms</h2>
              {forms.map(form =>
                  <div key={form.id}>
                    {form.name}
                  </div>
              )}
            </div>
          </header>
        </div>
    );
  }
}
export default App;


*/

/*
import logo from './logo.svg';
import './App.css';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;



 */

