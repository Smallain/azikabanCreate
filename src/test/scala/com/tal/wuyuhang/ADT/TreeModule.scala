package com.tal.wuyuhang.ADT

import com.tal.wuyuhang.Optionable.OptionModule._
import com.tal.wuyuhang.Functorble.FunctorModule._

object TreeModule extends App {
	
	sealed trait MyTree[+T] {
		def map[R](f: T => R): MyTree[R]
	}
	
	//递归定义
	case class Branch[T](left: MyTree[T], value: T, right: MyTree[T]) extends MyTree[T] {
		override def map[R](f: T => R): MyTree[R] = Branch(left.map(f), f(value), right.map(f))
	}
	
	case class Leaf[T](value: T) extends MyTree[T] {
		override def map[R](f: T => R): MyTree[R] = Leaf(f(value))
	}
	
	case object Empty extends MyTree[Nothing] {
		override def map[R](f: (Nothing) => R): MyTree[R] = Empty
	}
	
	private val bigtree: MyTree[Int] = Branch(Leaf(2), 1, Branch(Leaf(3), 4, Empty))
	
	//偏函数
	def rightMost[T]: MyTree[T] => MyOptional[T] = {
		case Leaf(v) => Just(v)
		case Branch(_, _, right) => rightMost(right)
		case Empty => NotExit
	}
	
	println(s"rightMost value is : ${rightMost(bigtree).map(_ * 2).map(_ + 1).getOrElse(-1)}")
	
	//	def allMulti2(tree: MyTree[Int]): MyTree[Int] = map(tree)(_ * 2)
	//
	//	def map[A, B](tree: MyTree[A])(f: A => B): MyTree[B] = tree match {
	//		case Leaf(v) => Leaf(f(v))
	//		case Branch(l, v, r) => Branch(map(l)(f), f(v), map(r)(f))
	//	}
	//
	//	println(allMulti2(bigtree))
	
	
	//调用 Functor
	
	
	def map[T[_], A, B](t: T[A])(f: A => B)(implicit functor: MyFunctor[T]): T[B] = {
		functor.map(t)(f)
	}
	
	
	//此步bigtree 类型必须是MyTree[Int]，因为Functor 只接受MyFunctor[MyTree]  Mytree类型
	println(map(bigtree)(_ * 5))
}