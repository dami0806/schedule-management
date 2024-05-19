package com.sparta.scheduleapp;

import com.sparta.scheduleapp.entity.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class persistenceTest {

    EntityManagerFactory emf;
    EntityManager em;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("schedule");
        em = emf.createEntityManager();
    }

    @Test
    @DisplayName("1차 캐시 : Entity 저장")
    void test1() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {
            Schedule schedule = new Schedule();
            schedule.setId(1L);
            schedule.setTitle("타이틀");
            schedule.setDescription("영속성 컨텍스트와 트랜잭션 이해하기");
            schedule.setAssignee("dami@example.com");
            schedule.setDate("2024-06-01");
            schedule.setPassword("비밀");

            em.merge(schedule);

            et.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하지 않은 경우")
    void test2() {
        try {

            Schedule schedule = em.find(Schedule.class, 1);
            System.out.println("schedule.getId() = " + schedule.getId());
            System.out.println("schedule.getTitle() = " + schedule.getTitle());
            System.out.println("schedule.getDescription() = " + schedule.getDescription());


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("Entity 조회 : 캐시 저장소에 해당하는 Id가 존재하는 경우")
    void test3() {
        try {

            // 첫 번째 find
            Schedule schedule = em.find(Schedule.class, 1);
            System.out.println("첫 번째 find 실행, schedule 조회 후 캐시 저장소에 저장");

            // 두 번째 find
            Schedule schedule2 = em.find(Schedule.class, 1);
            System.out.println("두 번째 find 실행");
            System.out.println("schedule2.getId() = " + schedule2.getId());
            System.out.println("schedule2.getTitle() = " + schedule2.getTitle());
            System.out.println("schedule2.getDescription() = " + schedule2.getDescription());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("객체 동일성 보장")
    void test4() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {
            Schedule schedule3 = new Schedule();
            schedule3.setId(2L);
            schedule3.setTitle("슬라이드");
            schedule3.setDescription("영속성 컨텍스트와 트랜잭션 이해하기");
            schedule3.setAssignee("dami@example.com");
            schedule3.setDate("2024-06-01");
            schedule3.setPassword("비밀");
            schedule3.setDeleted(false);

            em.persist(schedule3);

            Schedule schedule1 = em.find(Schedule.class, 1);
            Schedule schedule2 = em.find(Schedule.class, 1);
            Schedule schedule = em.find(Schedule.class, 2);

            System.out.println(schedule1 == schedule2);
            System.out.println(schedule1 == schedule);

            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("Entity 삭제")
    void test5() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {

            Schedule schedule = em.find(Schedule.class, 2);


            em.remove(schedule);

            et.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    @Test
    @DisplayName("쓰기 지연 저장소 확인")
    void test6() {
        EntityTransaction et = em.getTransaction();

        et.begin();

        try {
            Schedule schedule = new Schedule();
            schedule.setId(2L);
            schedule.setTitle("타이틀");
            schedule.setDescription("쓰기 지연 저장소");
            schedule.setAssignee("dami@example.com");
            schedule.setDate("2024-06-01");
            schedule.setPassword("비밀");
            em.persist(schedule);

            Schedule schedule2 = new Schedule();
            schedule2.setId(3L);
            schedule2.setTitle("타이틀2");
            schedule2.setDescription("과연 저장을 잘 하고 있을까?");
            schedule2.setAssignee("dami@example.com");
            schedule2.setDate("2024-06-01");
            schedule2.setPassword("비밀");
            em.persist(schedule2);


            System.out.println("트랜잭션 commit 전");
            et.commit();
            System.out.println("트랜잭션 commit 후");

        } catch (Exception ex) {
            ex.printStackTrace();
            et.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}
