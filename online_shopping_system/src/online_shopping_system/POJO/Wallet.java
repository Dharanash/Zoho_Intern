package online_shopping_system.POJO;

public class Wallet {
    public int walletId;
    public double balance;
    public int userId;
    public int point;
    public Wallet(int id, double balance, int userid, int point) {
        walletId=id;
        this.balance = balance;
        this.userId = userid;
        this.point = point;
    }

    public Wallet(int userid) {
        this.walletId=-1;
        this.balance = 0;
        this.userId = userid;
        this.point = 0;
    }
    
    public void addToWalletBalance(double amount) {
    	this.balance+=amount;
    }
    
    public void addToWalletBalance(int points) {
    	this.point+=points;
    }
}
