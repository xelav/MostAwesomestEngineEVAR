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
//
//!!!! Доработать вычисление импульса
//!!! Лишние контакты
//!!! Shape Vs Shape
//!!! Вращение
//!! Проблемы с методом Джарвиса - все еще есть вероятность дефектных форм
//! Отталкивание от стенок
//! Добавить больше тестов
//! Съезжание линии при малом радиусе
//! Статичные тела
//! Собственный getDeltaTime();