import React, { useEffect, useState } from "react";
import { User, UserAccount, AccountTransfer } from "../models/models";
import "../css/App.css";
import { updateAcc, doTransfer } from "../services/accountService";
import { AxiosError } from "axios";

interface TransferProps {
  user: User;
  setUser: React.Dispatch<React.SetStateAction<User>>;
  accountSelected: number;
}

const Transfer = ({
  user,
  setUser,
  accountSelected,
}: TransferProps) => {

  const [toAccountOptions, setToAccountOptions] = useState<UserAccount[]>()

  const [message, setMessage] = useState("");

  const [transfer, setTransfer] = useState<AccountTransfer>({
    fromAccount: accountSelected,
    toAccount: undefined,
    amount: undefined,
  });

  const submit = (e: any) => {
    e.preventDefault();
    const makeTransfer = async () => {      
      if(transfer.amount==undefined || !/^[0-9]*[.]?[0-9]+$/.test(transfer.amount.toString())) {
        setMessage("Incorrect input");        
        return;
      }
      try {
        const reply = await doTransfer(transfer, user.token);
        if (reply.length > 1) {
          setMessage("Transfer completed");
        } else {
          setMessage("error");
          return;
        }
      } catch (error) {
        if (error instanceof AxiosError) {
          setMessage("Insufficient funds on account");
        }
      }
    };
    makeTransfer();
    updateAccounts();
  };

  const updateAccounts = () => {
    const update = async () => {
      await new Promise((resolve) => setTimeout(resolve, 800));
      try {
        const reply = await updateAcc(user?.id, user.token);
        if (reply[0].balance>-1) {
          setUser({ ...user, accounts: reply });
        } else {
          setMessage("error");
          return;
        }
      } catch (error) {
        if (error instanceof AxiosError) {
          setMessage(error.message);
        }
      }
    };
    update();
  };

  const updateAccOpt = (()=> {
    let accOpt: UserAccount[] = user.accounts.filter((acc) => acc.accountNr != transfer.fromAccount);
      setToAccountOptions(accOpt)
  }) 

  const handleChange = (e: any) => {
    e.preventDefault();
    setTransfer({ ...transfer, [e.target.name]: e.target.value });    
    
    if (e.target.name != "toAccount") {
    updateAccOpt()
    }
  }; 
  
  useEffect(() => {
    updateAccounts();
    updateAccOpt()
  },[accountSelected]);

  useEffect(()=> {
    if(toAccountOptions && toAccountOptions.filter((opt) => opt.accountNr == transfer.toAccount).length == 0) {
    setTransfer({...transfer, toAccount: toAccountOptions[0].accountNr})
    }
  },[toAccountOptions])

  useEffect(()=> {
    updateAccOpt()
  },[user, transfer.fromAccount])

  
  return (
    <div className="Main">
      <form>
        <label>From account</label>
        <select
          onChange={handleChange}
          name={"fromAccount"}
          value={transfer.fromAccount}
        >
          {user &&
            user.accounts.map((account, index) => (
              <option key={index} value={`${account.accountNr}`}>
                {`${account.accountNr} (Balance: ${account.balance})`}
              </option>
            ))}
        </select>
        <label>To account</label>
        <select
          onChange={handleChange}
          name={"toAccount"}
          value={transfer.toAccount}
        >
          {user &&
            toAccountOptions?.map((account, index) => (
              <option key={index} value={`${account.accountNr}`}>
                {`${account.accountNr} (Balance: ${account.balance})`}
              </option>
            ))}
        </select>
        <label>Amount</label>
        <input
          type="text"
          name="amount"
          value={transfer.amount}
          onChange={handleChange}
        />
        {message && <p>{message}</p>}
        <button onClick={submit}>Submit</button>
      </form>
    </div>
  );
};

export default Transfer;
