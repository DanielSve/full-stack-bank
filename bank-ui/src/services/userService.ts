import axios from 'axios';
import { CreateUser, LoginUser } from '../models/models';

let config = {
  headers: {
    "Authorization": ""
  },
};

export const saveUser = async (user: CreateUser) => {
  const userRes = await axios.post('http://localhost:8080/user/add', user);
  return userRes.data;
};

export const loginUser = async (login: LoginUser) => {
  const id = await axios.post('http://localhost:8080/user/login', login, config);
  return id.data;
};

// export const getUserInfo = async (userId, token) => {
//   config.headers.Authorization = `Bearer ${token}`
//   const info = await axios.get(
//     `http://localhost:8080/user/getUserInfo/${userId}`
//   , config);
//   return info.data;
// };