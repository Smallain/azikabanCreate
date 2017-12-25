package com.tal.wuyuhang.Optionable

object OptionModule {
	
	sealed trait MyOptional[+T] {
		def get: T
		
		def isEmpty: Boolean
		
		def getOrElse[B >: T](default: B): B
		
		def map[R](f:T=>R):MyOptional[R]
	}
	
	case class Just[T](value: T) extends MyOptional[T] {
		override def get: T = value
		
		override def isEmpty: Boolean = false
		
		override def getOrElse[B >: T](default: B): B = value
		
		override def map[R](f: T => R): MyOptional[R] = Just(f(value))
	}
	
	case object NotExit extends MyOptional[Nothing] {
		override def get: Nothing = ???
		
		override def isEmpty: Boolean = true
		
		override def getOrElse[B >: Nothing](default: B): B = default
		
		override def map[R](f: Nothing => R): MyOptional[R] = NotExit
	}
	
}
