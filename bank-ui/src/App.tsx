import React, { useState } from "react";
import "./css/App.css";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
} from "react-router-dom";
import Login from "./components/Login";
import { User } from "./models/models";
import Profile from "./components/Profile";
import Account from "./components/Account";
import DepositWithdraw from "./components/DepositWithdraw";
import Transfer from "./components/Transfer";
import CreateNewUser from "./components/CreateNewUser";

function App() {

  const [user, setUser] = React.useState<User>({
    id: -1,
    name: "",
    accounts: [],
    token: ""
  });

  const [selectedAccount, setSelectedAccount] = useState<number>(0);

  return (
    <Router>
      <div className="App">
        <header>
          <div className="inner">
            <Link className='link' to='/'>
              <h1>Irori Bank</h1>
            </Link>
            <div className="nav">
              {!user.name && <Link className="link" to="/login">
                Login
              </Link> }
              {!user.name && <Link className="link" to="/createUser">
                Sign up
              </Link>}
              {user.name &&<Link className="link" to="/">
                Accounts
              </Link>}
              {user.name && <Link className="link" to="/login/reset">
                Logout
              </Link>}
            </div>
          </div>
        </header>
        <div >
          <Routes>
            <Route path='/login/:reset' element={<Login user={user} setUser={setUser} />} />
            <Route path='/createUser' element={<CreateNewUser user={user} setUser={setUser} />} />
            <Route path='/profile' element={<Profile user={user} setUser={setUser} selectedAccount={selectedAccount} setSelectedAccount={setSelectedAccount}/>} />
            <Route path='/deposit' element={<DepositWithdraw accountSelected={selectedAccount} user={user} setUser={setUser} transactType={"deposit"} />} />
            <Route path='/withdrawal' element={<DepositWithdraw accountSelected={selectedAccount} user={user} setUser={setUser} transactType={"withdrawal"} />} />
            <Route path='/transfer' element={<Transfer accountSelected={selectedAccount} user={user} setUser={setUser} />} />
            <Route
              path='/account/'
              element={<Account user={user} selectedAccount={selectedAccount}/>}
            />
            <Route path='/' element={<Login user={user} setUser={setUser} />} /> 
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
