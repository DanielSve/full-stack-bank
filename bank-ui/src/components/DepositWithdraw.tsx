import React, { useEffect, useState } from "react";
import { User } from "../models/models";
import "../css/App.css";
import { deposit, updateAcc, withdrawal } from "../services/accountService";
import { AxiosError } from "axios";

interface DepositWithdrawProps {
  user: User;
  transactType: string;
  setUser: React.Dispatch<React.SetStateAction<User>>;
  accountSelected: number;
}

const DepositWithdraw = ({
  user,
  setUser,
  transactType,
  accountSelected,
}: DepositWithdrawProps) => {

  const [message, setMessage] = useState("");

  const [transaction, setTransaction] = useState({
    accountNr: accountSelected,
    amount: "",
    type: transactType,
  });

  const submit = (e: any) => {
    e.preventDefault();
    if (transaction.amount=="" || !/^[0-9]*[.]?[0-9]+$/.test(transaction.amount.toString())) {
      setMessage("Incorrect input");
      return;
    }
    if (transaction.type === "deposit") {
      const makeDeposit = async () => {
        try {
          const reply = await deposit(transaction, user.token);
          if (reply.id) {
            setMessage("Deposit completed");
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
      makeDeposit();
      updateAccounts();
    } else {
      const makeWithdrawal = async () => {
        try {
          const reply = await withdrawal(transaction, user.token);
          if (reply.id) {
            setMessage("Withdrawal completed");
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
      makeWithdrawal();
      updateAccounts();
    }
  };

  const updateAccounts = () => {
    const update = async () => {
      await new Promise((resolve) => setTimeout(resolve, 800));
      try {
        const reply = await updateAcc(user?.id, user.token);
        if (reply[0].id) {
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

  const handleChange = (e: any) => {
    e.preventDefault();
    setTransaction({ ...transaction, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    updateAccounts();
  }, []);

  return (
    <div className="Main">
      <form>
        <label>Account</label>
        <select
          onChange={handleChange}
          name={"accountNr"}
          value={transaction.accountNr}
        >
          {user &&
            user.accounts.map((account, index) => (
              <option key={index} value={`${account.accountNr}`}>
                {`${account.accountNr} (Balance: ${account.balance})`}
              </option>
            ))}
        </select>
        <label>TransactionType</label>
        <select onChange={handleChange} name={"type"} value={transaction.type}>
          <option
            key={1}
            value={transactType === "deposit" ? `deposit` : "withdrawal"}
          >
            {transactType === "deposit" ? "Deposit" : "Withdrawal"}
          </option>
          <option
            key={2}
            value={transactType === "deposit" ? `withdrawal` : "deposit"}
          >
            {transactType === "deposit" ? "Withdrawal" : "Deposit"}
          </option>
        </select>
        <label>Amount</label>
        <input
          type="text"
          name="amount"
          value={transaction.amount}
          onChange={handleChange}
        />
        {message && <p>{message}</p>}
        <button onClick={submit}>Submit</button>
      </form>
    </div>
  );
};

export default DepositWithdraw;
