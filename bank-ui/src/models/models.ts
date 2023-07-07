export interface User {
  id: number;
  name: string;
  accounts: UserAccount[];
  token: string;
}

export interface CreateUser {
  name: string | number | readonly string[] | undefined
  ssn: string | number | readonly string[] | undefined
  password: string | number | readonly string[] | undefined
}

export interface LoginUser {
  ssn: string | number | readonly string[] | undefined
  password: string | number | readonly string[] | undefined;
}

export interface AccountTransfer {
  fromAccount: number;
  toAccount: number | undefined;
  amount: number | undefined;
}

export interface UserAccount {
   id: number;
   accountNr: number;
   balance: number 
}

export interface AccountWithTransactions {
    account: UserAccount;
    transactions: Transaction[];
}

export interface Transaction {
   amount: number;
   transactionType: String;
   timestamp: String 
}
