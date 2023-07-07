import axios from 'axios';

let config = {
  headers: {
    Authorization: "",
  },
};


export const getAccount = async (accountNr: number, token: string) => { 
  config.headers.Authorization = `Bearer ${token}` 
  const res = await axios.post(`http://localhost:8080/account/get/${accountNr}`,null, config);
  return res.data;
};

export const createAccount = async (id: number, token: string) => { 
  config.headers.Authorization = `Bearer ${token}` 
  console.log(config.headers);
  
  const res = await axios.post(`http://localhost:8080/account/add/${id}`,null, config);
  return res.data;
};

export const deposit = async (deposit:any, token: string) => { 
  config.headers.Authorization = `Bearer ${token}` 
  const res = await axios.post(`http://localhost:8080/account/deposit`, deposit, config);
  return res.data;
};

export const withdrawal = async (deposit:any, token: string) => {  
  config.headers.Authorization = `Bearer ${token}`
  const res = await axios.post(`http://localhost:8080/account/withdrawal`, deposit, config);
  return res.data;
};

export const updateAcc = async (userId: number | undefined, token: string) => {
  config.headers.Authorization = `Bearer ${token}`
  const res = await axios.post(`http://localhost:8080/account/getByUserId/${userId}`, null, config);
  return res.data;
}

export const doTransfer = async (transfer: any, token: string) => {
  config.headers.Authorization = `Bearer ${token}`
  const res = await axios.post(`http://localhost:8080/account/transfer`, transfer, config);
  return res.data;
}
