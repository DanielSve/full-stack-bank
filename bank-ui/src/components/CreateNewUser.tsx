import React, { useEffect } from 'react';
import { saveUser } from '../services/userService';
import { useNavigate } from 'react-router-dom';
import { User, CreateUser } from '../models/models';
import "../css/App.css";
import { AxiosError } from 'axios';

interface CreateUserProps {
  user: User
  setUser: React.Dispatch<React.SetStateAction<User>>
}

const CreateNewUser = ({user, setUser} : CreateUserProps) => {
  let navigate = useNavigate();
  
  const [createUserAcc, setCreateUserAcc] = React.useState<CreateUser>({
    name: '',
    ssn: '',
    password: ""
  });

  const [message, setMessage] = React.useState('');

  const handleChange = (e: any) =>
    setCreateUserAcc({ ...createUserAcc, [e.target.name]: e.target.value });

  const submit = (e: any) => {
    e.preventDefault();

    const create = async () => {
      try {
        const reply = await saveUser(createUserAcc);
        if (reply.id) {
          setMessage('Success');
          navigate("/login/1")
        } else {
          setMessage('Unable to create account');
        }
      } catch (error) {
        if (error instanceof AxiosError) {
          setMessage(error.message);
        }
      }
    };
    create();
  };

  useEffect(()=> {
    user.name && navigate("/profile")
  },[])

  return ( 
    <div className='Main'>
      {!user.name && <><h3>Please fill in your details</h3>
      <form>
        <label>Name</label>
        <input
          type='text'
          name='name'
          value={createUserAcc.name}
          onChange={handleChange}
        />
        <label>Social security number</label>
        <input
          type='text'
          name='ssn'
          value={createUserAcc.ssn}
          onChange={handleChange}
        />
        <label>Password</label>
        <input
          type='text'
          name='password'
          value={createUserAcc.password}
          onChange={handleChange}
        />
        <p>{message}</p>
        <button onClick={submit}>Submit</button>
      </form>
      </>}
    </div>
  );
};

export default CreateNewUser;