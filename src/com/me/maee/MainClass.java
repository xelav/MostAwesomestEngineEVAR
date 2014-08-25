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

//? ����� �����-����� � ������� �������
//? ���������� �� ����� �� ������
//? ����������� ���������� ��������� �����
//? ���������� � ��������� ������
//? ��������� �������� ��������
//
//!!!! ���������� ���������� ��������
//!!! ������ ��������
//!!! Shape Vs Shape
//!!! ��������
//!! �������� � ������� �������� - ��� ��� ���� ����������� ��������� ����
//! ������������ �� ������
//! �������� ������ ������
//! ��������� ����� ��� ����� �������
//! ��������� ����
//! ����������� getDeltaTime();