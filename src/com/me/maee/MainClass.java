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
//?! описывающая окружность множества точек
//? Собственный getDeltaTime();
//? Вычисления с плавающей точкой
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
//!!!! Вращение
//? АлгоритмЫ проверки коллизий
//!! Доработать вычисление импульса
//!!! Shape Vs Shape
//!! Отталкивание от стенок
//!! Добавить больше тестов
//!! Генератор случайных выпуклых многоугольников
//! Съезжание линии при малом радиусе
//? Что если учитывать разброс радиусов в QuadTree?
// Статичные тела
//Почему-то круг смещается по 0Y на двадцать пикселей вниз - Проблема устранена до тех пор, пока не буду вводить вращения
//При вызове defineMass() все исчезает - почему-то заработало само собой, после удаления mass из аргументов. Конфликт с defineMass?
//Точка рендерится не туда - опять разбираться с системой координат - проблема временно исправлена, но с этим все еще надо разобраться
//Подумать насчет едениц измерения, в будущем могут быть проблемы с этим