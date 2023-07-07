import React from "react";
import { useNavigate } from "react-router-dom";
import { User } from "../models/models";
import "../css/App.css";
import { createAccount, updateAcc } from "../services/accountService";
import { AxiosError } from "axios";

interface UserProps {
  user: User;
  setUser: React.Dispatch<React.SetStateAction<User>>; 
  selectedAccount: number;
  setSelectedAccount: React.Dispatch<React.SetStateAction<number>>;
}

const Profile = ({ user, setUser, setSelectedAccount }: UserProps) => {
  let navigate = useNavigate();

  const handleClick = (e: any) => {
    const setAccount = async () => {
      setSelectedAccount(Number(e.target.id));
      await new Promise((resolve) => setTimeout(resolve, 600));
    };    
    setAccount();
    navigate("/account");
  };

  const create = () => {
    const createAcc = async () => {
      if (user) {
        try {
          const reply = await createAccount(user.id, user.token);         
          if (reply.accountNr) {
            console.log("Success");
          }
        } catch (err) {
          console.log("Could not create account");
        }
      }
    };
    createAcc();
    updateAccounts();
  };

  const updateAccounts = () => {
    const update = async () => {
      await new Promise((resolve) => setTimeout(resolve, 800));
      try {
        const reply = await updateAcc(user?.id, user.token);
        if (reply[0].id) {
          setUser({ ...user, accounts: reply });
        } else {
          console.log("eCould not update accounts");
          return;
        }
      } catch (error) {
        if (error instanceof AxiosError) {
          console.log(error.message);
        }
      }
    };
    update();
  };

  return (
    <div className="Main">
      <div className="profile-card">
      <h3>Welcome {user && user.name}!</h3>
      </div>
      <div className="profile-card">
        <h3>Accounts</h3>
        {user?.accounts &&
          user.accounts.map((acc) => (
            <>
              <button
                className="account-btn"
                id={`${acc.accountNr}`}
                onClick={handleClick}
              >
                <span className="btn-span">
                  {`Account - ${acc.accountNr}`}{" "}
                </span>
                {` | $${acc.balance}`}
                <span className="btn-span-2">{`>`}</span>
              </button>
              <br></br>
            </>
          ))}
        <button onClick={create}>New Account</button>
      </div>
    </div>
  );
};

export default Profile;
