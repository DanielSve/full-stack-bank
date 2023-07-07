import React, { useEffect } from 'react';
import { loginUser } from '../services/userService';
import { useNavigate, useParams } from 'react-router-dom';
import { User, LoginUser } from '../models/models';
import "../css/App.css";

interface LoginProps {
  user: User
  setUser: React.Dispatch<React.SetStateAction<User>>
}

const Login = ({user, setUser} : LoginProps) => {
  let navigate = useNavigate();
  
  let { reset } = useParams();
  
  const [login, setLogin] = React.useState<LoginUser>({
    ssn: '',
    password: ''
  });

  const [message, setMessage] = React.useState('Login');

  const handleChange = (e: any) =>
    setLogin({ ...login, [e.target.name]: e.target.value });

  const submit = (e: any) => {
    e.preventDefault();
    const loginU = async () => {
      try {
        const reply = await loginUser(login);
          setMessage('Success');
          setUser({
            id: reply.id,
            name: reply.name,
            accounts: reply.accounts,
            token: reply.token
          });
          navigate("/profile")
      } catch (err) {
        setMessage("Unable to login")
      }
    };
    loginU();
  };

  useEffect(()=> {    
    user.name && navigate("/profile")
  },[])

  useEffect(()=> {
    if(reset=="reset") {
      setUser({
        id: -1,
        name: "",
        accounts: [],
        token: ""
      }); 
      navigate("/login/reset")
    }
  },[reset])

  return ( 
    <div className='Main'>
      {!user.name && <><h3>Login</h3>
      <form>
        <label>Social security number</label>
        <input
          type='text'
          name='ssn'
          value={login.ssn}
          onChange={handleChange}
        />
        <label>Password</label>
        <input
          type='text'
          name='password'
          value={login.password}
          onChange={handleChange}
        />
        <p>{message}</p>
        <button onClick={submit}>Submit</button>
      </form>
      <p>Don't have an account?</p>
      <button onClick={() => {navigate("/createUser");}}>Create Account</button>
      </>}
    </div>
  );
};

export default Login;