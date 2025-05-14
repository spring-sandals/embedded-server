package com.sandals.accounts

import java.io.Serializable

class Account(val id: Long, val name: String, val value: Double = 0.00) : Serializable