
class TransactionService()
{

    fun createTransaction(amount: Int,name:String) {
        if (amount <= 0) {
            throw IllegalArgumentException("Amount must be greater than 0")
        }
        if(name.length<=0)
        {
            throw IllegalArgumentException("Name cant be nullable")
        }

    }
    fun removeTransaction()
    {

    }
    fun getTransactions()
    {

    }

}