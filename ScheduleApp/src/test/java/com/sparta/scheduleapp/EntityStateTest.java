package com.sparta.scheduleapp;

import com.sparta.scheduleapp.entity.Schedule;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class EntityStateTest {

    @PersistenceContext
    private EntityManager em;

    //    @BeforeEach
//    void setUp() {
//        emf = Persistence.createEntityManagerFactory("schedule");
//        em = emf.createEntityManager();
//    }
    @Test
    @DisplayName("비영속과 영속 상태")
    @Transactional
    void test1() {
        Schedule schedule1 = new Schedule(); // 비영속 상태
        schedule1.setTitle("비영속제목");
        schedule1.setDescription("비영속과 영속 상태");
        schedule1.setAssignee("Assignee");
        schedule1.setDate("2023-05-19");
        schedule1.setPassword("password");


        Schedule schedule2 = new Schedule();
        schedule2.setTitle("비영속제목2");
        schedule2.setDescription("비영속과 영속 상태2");
        schedule2.setAssignee("Assignee2");
        schedule2.setDate("2023-05-19");
        schedule2.setPassword("password2");


        System.out.println("영속 상태 전: " + em.contains(schedule1)); // false


        em.persist(schedule1);
        em.persist(schedule2);

        em.flush();
    }

    @Test
    @DisplayName("준영속 상태 : detach()")
    @Transactional
    void test2() {

        Schedule schedule = em.find(Schedule.class, 1L);

        System.out.println("schedule.getId() = " + schedule.getId());
        System.out.println("schedule.getTitle() = " + schedule.getTitle());
        System.out.println("schedule.getDescription() = " + schedule.getDescription());

        // em.contains(entity) : Entity 객체가 현재 영속성 컨텍스트에 저장되어 관리되는 상태인지 확인하는 메서드
        System.out.println("em.contains(schedule) = " + em.contains(schedule));

        System.out.println("detach() 호출");
        em.detach(schedule);
        System.out.println("em.contains(schedule) = " + em.contains(schedule));

        System.out.println("schedule Entity 객체 수정 시도");
        schedule.setTitle("Update");
        schedule.setDescription("schedule Entity Update");

        System.out.println("트랜잭션 commit 전");
        em.flush();

        System.out.println("트랜잭션 commit 후");
    }


    @Test
    @DisplayName("준영속 상태 : clear()")
    @Transactional
    void test3() {

        Schedule schedule1 = em.find(Schedule.class, 1L);
        Schedule schedule2 = em.find(Schedule.class, 2);
        if (schedule1 != null && schedule2 != null) {
            // em.contains(entity) : Entity 객체가 현재 영속성 컨텍스트에 저장되어 관리되는 상태인지 확인하는 메서드
            System.out.println("em.contains(schedule1) = " + em.contains(schedule1));
            System.out.println("em.contains(schedule2) = " + em.contains(schedule2));

            System.out.println("clear() 호출");
            em.clear();
            System.out.println("em.contains(1) = " + em.contains(schedule1));
            System.out.println("em.contains(schedule2) = " + em.contains(schedule2));

            System.out.println("schedule#1 Entity 다시 조회");
            Schedule schedule = em.find(Schedule.class, 1);
            System.out.println("em.contains(schedule) = " + em.contains(schedule));
            System.out.println("\n schedule Entity 수정 시도");
            schedule.setTitle("Update");
            schedule.setDescription("schedule Entity Update");

            em.flush();
            System.out.println("트랜잭션 commit 전");
        } else {
            System.out.println("ID가 1인 Schedule 엔티티 또는 ID가 2인 Schedule 엔티티를 찾을 수 없습니다.");
        }
    }

}
