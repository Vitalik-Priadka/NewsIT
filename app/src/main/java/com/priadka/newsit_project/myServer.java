package com.priadka.newsit_project;

import com.priadka.newsit_project.DTO.NewsDTO;
import com.priadka.newsit_project.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class myServer {
    private static UserDTO user = null;
    private static List<NewsDTO> data = null;

    // Временная заглушка для статей
    public static void createMockListNews() {
        data = new ArrayList<>();
        data.add(new NewsDTO(101 ,1, "Title#1 Статья о Роботах","Текс статьи №1","13:57 22.10.17", 50, 26));
        data.add(new NewsDTO(102 ,2, "Title#2 Статья о Взрывах","Текс статьи №2","12:12 14.07.17", 124, 45));
        data.add(new NewsDTO(103 ,3, "Title#3 Статья о Космосе","Текс статьи №3","16:28 26.08.17", 24, 4));
        data.add(new NewsDTO(104 ,4, "Title#4 Статья о Рыбалке Эм..","Текс статьи №4","17:34 06.08.17", 43, 3));
        data.add(new NewsDTO(105 ,5, "Title#5 Статья о Кибернетике","Текс статьи №5","19:13 23.09.17", 76, 63));
        data.add(new NewsDTO(106 ,6, "Title#6 Статья о Пришельцах","Текс статьи №6","12:13 02.09.17", 86, 65));
        data.add(new NewsDTO(107 ,7, "Title#7 Статья о Xiaomi","Текс статьи №7","18:27 23.09.17", 36, 13));
        data.add(new NewsDTO(108 ,8, "Title#8 Статья о Apple","Текс статьи №8","13:53 23.09.17", 96, 23));
        data.add(new NewsDTO(109 ,9, "Title#9 Статья о Samsung","Текс статьи №9","12:49 23.09.17", 36, 34));
        data.add(new NewsDTO(110 ,10, "Title#10 Статья о Машинах","Текс статьи №10","19:13 23.09.17", 999, 228));
    }

    // Временна заглушка информации пользователя
    public static void initMockUser() {
        ArrayList<Integer> listBookmark = new ArrayList<Integer>();
        listBookmark.add(102); listBookmark.add(105);
        user = new UserDTO("Vitalik","vitalik.pryadka@gmail.com", "12345", 3 , listBookmark);
    }
    public static UserDTO getUser(){if(user == null)initMockUser(); return user;}
    public static List<NewsDTO> getNews(){if(data == null)createMockListNews(); return data;}
}
