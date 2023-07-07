import { useEffect, useState } from "react";
import { Link} from "react-router-dom";
import {
  User,
  AccountWithTransactions,
} from "../models/models";
import "../css/App.css";
import { getAccount } from "../services/accountService";

interface AccountProps {
  user: User | undefined;
  selectedAccount: number;
}

const Account = ({ user, selectedAccount }: AccountProps) => {

  const [accountExtended, setAccountExtended] =
    useState<AccountWithTransactions>();

  useEffect(() => {
    if (selectedAccount != 0 && user != undefined) {
      const getTransact = async () => {
        const reply = await getAccount(Number(selectedAccount), user.token);
        setAccountExtended(reply);
      };
      getTransact();
    }
  }, [selectedAccount]);

  const formatTimestamp = (timestamp: String) => {
    timestamp = timestamp.replace(/T/g, " ");
    return timestamp.substring(0, 19)
  }

  return (
    <div className="Main">
      {accountExtended && (
        <>
          <div className="profile-card">
            {accountExtended?.account?.accountNr && (
              <>
                <h3>{`Account ${accountExtended.account.accountNr}`}</h3>
                <p>{`Balance: ${accountExtended?.account.balance}`}</p>
                <Link to={"/deposit"}>
                  <button>Deposit</button>
                </Link>
                <Link to={"/withdrawal"}>
                  <button>Withdrawal</button>
                </Link>
                <Link to={"/transfer"}>
                  <button>Transfer</button>
                </Link>
              </>
            )}
          </div>
          <div className="profile-card">
            <h4>Transactions:</h4>
            {accountExtended.transactions &&
              accountExtended.transactions.map((t) => (
                <p>{`${formatTimestamp(t.timestamp)} ${t.transactionType} ${t.amount}`}</p>
              ))}
          </div>
        </>
      )}
    </div>
  );
};

export default Account;
