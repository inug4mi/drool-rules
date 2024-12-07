package com.udea.reglas.model;

public class Participant {
    private String name;
    private int age;
    private int creditScore;
    private long annualSalary;
    private long existingDebt;
    private long loanAmount;
    private String employmentType; // Full-time, Part-time, Self-employed, Unemployed
    private int yearsWithBank; // Years as a bank client
    private boolean hasSavingsAccount;
    private long investmentPortfolioValue; // Value of investment portfolio
    private long monthlyExpenses;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public long getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(long annualSalary) {
        this.annualSalary = annualSalary;
    }

    public long getExistingDebt() {
        return existingDebt;
    }

    public void setExistingDebt(long existingDebt) {
        this.existingDebt = existingDebt;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public int getYearsWithBank() {
        return yearsWithBank;
    }

    public void setYearsWithBank(int yearsWithBank) {
        this.yearsWithBank = yearsWithBank;
    }

    public boolean isHasSavingsAccount() {
        return hasSavingsAccount;
    }

    public void setHasSavingsAccount(boolean hasSavingsAccount) {
        this.hasSavingsAccount = hasSavingsAccount;
    }

    public long getInvestmentPortfolioValue() {
        return investmentPortfolioValue;
    }

    public void setInvestmentPortfolioValue(long investmentPortfolioValue) {
        this.investmentPortfolioValue = investmentPortfolioValue;
    }

    public long getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(long monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }
}
