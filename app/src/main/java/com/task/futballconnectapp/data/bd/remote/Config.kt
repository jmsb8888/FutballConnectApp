package com.task.futballconnectapp.data.bd.remote

import java.sql.Connection
import java.sql.DriverManager

object DatabaseConfig {
    init {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(
                "SQL Server JDBC Driver not found. Include it in your library path",
                e
            )
        }
    }

    private const val JDBC_URL =
        "jdbc:jtds:sqlserver://FutballConnectApp.mssql.somee.com:1433/FutballConnectApp;" +
                "user=jmsb88_SQLLogin_1;" +
                "password=qndan3t32a;" +
                "encrypt=false;"


    fun getConnection(): Connection {
        return DriverManager.getConnection(JDBC_URL)
    }
}
