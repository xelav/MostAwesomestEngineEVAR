package com.me.maee;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MainClass {
	public static void main(String[] args) {
			LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
			cfg.title = "EpicEngine";
			cfg.useGL20 = true;
			cfg.fullscreen = false;
			cfg.vSyncEnabled = true;
			
			new LwjglApplication(new MAE(), cfg);
		}
	}

//? метод Рунге-Кутта и формула Тейлора
//? расстояние от точки до прямой
//? описывающая окружность множества точек
//? Вычисления с плавающей точкой
//? АлгоритмЫ проверки коллизий
//? Что если учитывать разброс радиусов в QuadTree?
// 1. Работа с силами, массами, импульсами, скоростями
// - velocity - CHECK
//   impulse  - CHECK
//   force    - CHECK
//   mass     - CHECK
//   rotation
// 2. Обработка коллизий
//	Вызов проверки для всех объектов - CHECK
//	? Предварительная проверка
//	Разрешение контактов - CHECK
//	? Тунелирование
//	? Хеш-ячейки
// 3. Оптимизация!
//	QuadTree - CHECK
// 	Groups of objects
// 4. Меню с настройками
//
//
//!!! Shape Vs Shape
//!!! Вращение
//!! (Б)анальные утехи с методом Джарвиса - все еще есть вероятность дефектных форм
//!! Доработать вычисление импульса
//!! Отталкивание от стенок
//! Добавить больше тестов
//! Съезжание линии при малом радиусе
//! Статичные тела
//! Собственный getDeltaTime();
//Почему-то круг смещается по 0Y на двадцать пикселей вниз - Проблема устранена до тех пор, пока не буду вводить вращения