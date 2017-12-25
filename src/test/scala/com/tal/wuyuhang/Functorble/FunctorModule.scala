package com.tal.wuyuhang.Functorble

import com.tal.wuyuhang.ADT.ListModule._
import com.tal.wuyuhang.ADT.TreeModule._

object FunctorModule {
	
	trait MyFunctor[T[_]] {
		def map[A, B](t: T[A])(f: A => B): T[B]
	}
	
	implicit object FunctorMyTree extends MyFunctor[MyTree] {
		override def map[A, B](t: MyTree[A])(f: A => B): MyTree[B] = t match {
			case Leaf(v) => Leaf(f(v))
			case Branch(l, v, r) => Branch(map(l)(f), f(v), map(r)(f))
			case Empty=>Empty
		}
	}
	
	implicit object FunctorMyList extends MyFunctor[MyList] {
		override def map[A, B](t: MyList[A])(f: A => B): MyList[B] = t match {
			case MyNil => MyNil
			case Cons(h, tail) => Cons(f(h), map(tail)(f))
		}
	}
	
}
