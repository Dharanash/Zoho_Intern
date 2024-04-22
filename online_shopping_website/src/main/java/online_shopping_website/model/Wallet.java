package online_shopping_website.model;

public class Wallet {
    public double balance;
    public int userId;
    public int point;
    public Wallet(double balance, int userid, int point) {
        this.balance = balance;
        this.userId = userid;
        this.point = point;
    }

    public Wallet(int userid) {
        this.balance = 0;
        this.userId = userid;
        this.point = 0;
    }
    
    public void addAmountToWalletBalance(double amount) {
    	this.balance+=amount;
    }
    
    public void addPointsToWallet(int points) {
    	this.point+=points;
    }
    
    public void redeemToWalletBalance() {
    	this.balance+=(double)(point/10);
    	point=0;
    }

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
    
    
}
