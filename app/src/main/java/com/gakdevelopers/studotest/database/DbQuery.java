package com.gakdevelopers.studotest.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.models.CategoryModel;
import com.gakdevelopers.studotest.models.NotificationsModel;
import com.gakdevelopers.studotest.models.Profile;
import com.gakdevelopers.studotest.models.Question;
import com.gakdevelopers.studotest.models.TestsModel;
import com.gakdevelopers.studotest.models.Rank;
import com.gakdevelopers.studotest.models.ViewAnswer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbQuery {
    public static FirebaseFirestore g_fireStore;

    public static List<CategoryModel> g_catList = new ArrayList<>();

    public static int g_selected_cat_index = 0;

    public static int g_app_version = 0;

    public static List<TestsModel> g_testList = new ArrayList<>();

    public static List<String> g_couponList = new ArrayList<>();

    public static List<Integer> g_my_courses_list_indexes = new ArrayList<>();

    public static List<String> g_my_courses_list = new ArrayList<>();

    public static List<String> g_home_posters = new ArrayList<>();

    public static List<Integer> g_all_leader_scores = new ArrayList<>();

    public static List<NotificationsModel> g_notifications = new ArrayList<>();

    public static int g_selected_test_index = 0;

    public static List<Question> g_question_list = new ArrayList<>();

    public static List<ViewAnswer> g_view_answers_list = new ArrayList<>();

    public static Profile myProfile = new Profile("NA", null);

    public static Rank myPerformance = new Rank("NULL", 0, -1);

    public static List<Rank> g_users_list = new ArrayList<>();

    public static int g_positive_marks = 0;
    public static int g_negative_marks = 0;
    public static int g_price = 0;

    public static int g_attempt = 0;

    public static int g_users_count = 0;

    public static int g_rank = 0;

    public static boolean iAmInTopList = false;

    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;

    public static void getTopUsers(MyCompleteListener completeListener) {
        g_users_list.clear();

        String myUId = FirebaseAuth.getInstance().getUid();

        g_fireStore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE", 0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            g_users_list.add(new Rank(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));

                            if (myUId.compareTo(doc.getId()) == 0) {
                                iAmInTopList = true;
                                myPerformance.setRank(rank);
                            }

                            rank++;
                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void checkForUpdates(MyCompleteListener completeListener) {
        g_fireStore.collection("APP_VERSION")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                g_app_version = Math.toIntExact(d.getLong("VERSION_CODE"));
                            }

                            Log.d("V_C_", String.valueOf(g_app_version));
                        } else {
                            completeListener.onFailure();
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadHomePosters(MyCompleteListener completeListener) {
        g_home_posters.clear();

        g_fireStore.collection("HOME_POSTERS").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                g_home_posters.add(doc.getString("URL"));
                            }

                        }

                        completeListener.onSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadLeaderboard(int myScore, MyCompleteListener completeListener) {

        g_all_leader_scores.clear();

        g_fireStore.collection("LEADERBOARD").document("TESTS_LIST").collection(g_testList.get(g_selected_test_index).getTestId())
                .whereGreaterThan("SCORE", 0)
                .orderBy("SCORE", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                            g_all_leader_scores.add(Integer.valueOf(doc.get("SCORE").toString()));

                            Log.d("ALL_SCORES", String.valueOf(doc.get("SCORE")));
                        }

                        Collections.sort(g_all_leader_scores, new Comparator<Integer>() {
                            @Override
                            public int compare(Integer o1, Integer o2) {
                                return o2.compareTo(o1);
                            }
                        });

                        Log.d("ALL_SCORES_SORTED", String.valueOf(g_all_leader_scores));

                        g_rank = 1;

                        if (myScore == 0) {
                            g_rank = g_all_leader_scores.size();
                        } else {
                            for (int s = 0; s < g_all_leader_scores.size(); s++) {
                                if (g_all_leader_scores.get(s) > myScore)
                                    g_rank++;
                                else
                                    break;
                            }
                        }

                        Log.d("ALL_SCORES_MY_RANK", String.valueOf(g_rank));

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });


    }

    public static void getUsersCount(MyCompleteListener completeListener) {
        g_fireStore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_users_count = documentSnapshot.getLong("COUNT").intValue();

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void checkMyCourses(MyCompleteListener completeListener) {
        g_my_courses_list_indexes.clear();
        g_my_courses_list.clear();

        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_COURSES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < g_catList.size(); i++) {
                            if (documentSnapshot.get(String.valueOf(g_catList.get(i).getName())) != null) {
                                Log.d("COURSE_NAMES", String.valueOf(documentSnapshot.get(String.valueOf(g_catList.get(i).getDocId()))));
                                g_my_courses_list_indexes.add(i);
                            }
                        }

                        for (int a : g_my_courses_list_indexes) {
                            g_my_courses_list.add(String.valueOf(g_catList.get(a).getName()));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadMyCourses(MyCompleteListener completeListener) {
        g_catList.clear();

        g_fireStore.collection("PAID_TESTS").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            docList.put(doc.getId(), doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        long catCount = catListDoc.getLong("COUNT");

                        for (int i  = 1; i <= catCount; i++) {
                            String catId = catListDoc.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catId);

                            int noOfTests = catDoc.getLong("NO_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");

                            if (g_my_courses_list_indexes.contains(i-1))
                                g_catList.add(new CategoryModel(catId, catName, noOfTests));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadNotifications(MyCompleteListener completeListener) {
        g_notifications.clear();

        g_fireStore.collection("NOTIFICATIONS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                g_notifications.add(new NotificationsModel(d.getString("TITLE"), d.getString("DESC"), d.getString("TIME")));
                            }

                            Collections.sort(g_notifications, new Comparator<NotificationsModel>() {
                                @Override
                                public int compare(NotificationsModel o1, NotificationsModel o2) {
                                    return o2.getTime().compareTo(o1.getTime());
                                }
                            });
                        } else {
                            completeListener.onFailure();
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void getUserData(final MyCompleteListener completeListener) {
        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));

                        //myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        myPerformance.setName(documentSnapshot.getString("NAME"));

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void createUser(String email, String name, final MyCompleteListener completeListener) {
        Map<String, Object> userData = new ArrayMap<>();

        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        //userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_fireStore.batch();
        batch.set(userDoc, userData);

        DocumentReference countDoc = g_fireStore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void updateProfile(String email, String name, final MyCompleteListener completeListener) {
        Map<String, Object> updatedData = new ArrayMap<>();

        updatedData.put("EMAIL_ID", email);
        updatedData.put("NAME", name);

        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .update(updatedData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        myProfile.setEmail(email);

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadCategories(String testType, final MyCompleteListener completeListener) {
        g_catList.clear();

        g_fireStore.collection(testType).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            docList.put(doc.getId(), doc);
                        }

                        //QueryDocumentSnapshot catListDoc = docList.get("Categories");
                        QueryDocumentSnapshot catListDoc = docList.get("Categories");

                        long catCount = catListDoc.getLong("COUNT");

                        for (int i  = 1; i <= catCount; i++) {
                            String catId = catListDoc.getString("CAT" + String.valueOf(i) + "_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catId);

                            int noOfTests = catDoc.getLong("NO_OF_TESTS").intValue();
                            String catName = catDoc.getString("NAME");

                            g_catList.add(new CategoryModel(catId, catName, noOfTests));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void loadCouponCodes(MyCompleteListener completeListener) {
        g_couponList.clear();

        g_fireStore.collection("COUPON_CODES")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                g_couponList.add(document.getId());
                            }
                            //Log.d(TAG, list.toString());
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }

    public static void deleteUsedCoupon(String couponCode, MyCompleteListener completeListener) {
        g_fireStore.collection("COUPON_CODES")
                .document(couponCode)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadTests(String testType, final MyCompleteListener completeListener) {
        g_testList.clear();

        DocumentReference ref = g_fireStore.collection(testType).document(g_catList.get(g_selected_cat_index).getDocId());

        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                g_positive_marks = value.getLong("POSITIVE").intValue();
                g_negative_marks = value.getLong("NEGATIVE").intValue();

                if (testType.equals("PAID_TESTS")) {
                    g_price = value.getLong("PRICE").intValue();
                }

            }
        });

        g_fireStore.collection(testType).document(g_catList.get(g_selected_cat_index).getDocId())
                .collection("TESTS_LIST").document("TEST_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTests = g_catList.get(g_selected_cat_index).getNoOfTest();

                        for (int i = 1; i <= noOfTests; i++) {
                            g_testList.add(new TestsModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) + "_TIME").intValue(),
                                    0,
                                    Boolean.TRUE.equals(documentSnapshot.getBoolean("TEST" + String.valueOf(i) + "_LIVE"))
                                    //documentSnapshot.getBoolean("TEST" + String.valueOf(i) + "_LIVE")
                            ));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadData(String testType, final MyCompleteListener completeListener) {
        loadCategories(testType, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(completeListener);
                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }

    public static void loadQuestions(MyCompleteListener completeListener) {
        g_question_list.clear();

        g_fireStore.collection("QUESTIONS")
                .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocId())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_question_list.add(new Question(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    doc.getString("EXPLANATION"),
                                    NOT_VISITED
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadViewAnswers(MyCompleteListener completeListener) {
        g_view_answers_list.clear();

        g_fireStore.collection("QUESTIONS")
                .whereEqualTo("CATEGORY", g_catList.get(g_selected_cat_index).getDocId())
                .whereEqualTo("TEST", g_testList.get(g_selected_test_index).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            g_view_answers_list.add(new ViewAnswer(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    doc.getString("EXPLANATION")
                            ));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadMyScores(MyCompleteListener completeListener) {
        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < g_testList.size(); i++) {
                            int top = 0;
                            if (documentSnapshot.get(g_testList.get(i).getTestId()) != null) {
                                top = documentSnapshot.getLong(g_testList.get(i).getTestId()).intValue();
                            }
                            g_testList.get(i).setTopScore(top);
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

        g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_ATTEMPTS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for (int i = 0; i < g_testList.size(); i++) {
                            g_attempt = 0;

                            if (documentSnapshot.get(g_testList.get(i).getTestId()) != null) {
                                g_attempt = documentSnapshot.getLong(g_testList.get(i).getTestId()).intValue();
                            }

                            g_testList.get(i).setAttempt(g_attempt);
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void savePurchaseData(int price, MyCompleteListener completeListener) {
        WriteBatch batch = g_fireStore.batch();

        DocumentReference userDoc = g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

        DocumentReference courseDoc = userDoc.collection("USER_DATA").document("MY_COURSES");

        Map<String, Object> purchaseData = new ArrayMap<>();

        purchaseData.put(g_catList.get(g_selected_cat_index).getName(), price);

        batch.set(courseDoc, purchaseData, SetOptions.merge());

        batch.commit()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void saveResult(int scoreFromResult, MyCompleteListener completeListener) {
        WriteBatch batchAttempt = g_fireStore.batch();
        WriteBatch batchScore = g_fireStore.batch();
        WriteBatch batchLeaderboard = g_fireStore.batch();

        g_attempt = 0;

        DocumentReference userDoc = g_fireStore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

        if (g_testList.get(g_selected_test_index).getAttempt() <= 3) {
            DocumentReference attemptDoc = userDoc.collection("USER_DATA").document("MY_ATTEMPTS");

            Map<String, Object> attemptData = new ArrayMap<>();
            attemptData.put(g_testList.get(g_selected_test_index).getTestId(), g_testList.get(g_selected_test_index).getAttempt() + 1);

            batchAttempt.set(attemptDoc, attemptData, SetOptions.merge());
        }

        batchAttempt.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (g_testList.get(g_selected_test_index).getAttempt() <= 3) {
                            g_testList.get(g_selected_test_index).setAttempt(g_attempt);

                            completeListener.onSuccess();
                        } else {
                            completeListener.onSuccess();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

        DocumentReference scoreSoc = userDoc.collection("USER_DATA").document("MY_SCORES");

        Map<String, Object> scoreDataProfile = new ArrayMap<>();
        scoreDataProfile.put(g_testList.get(g_selected_test_index).getTestId(), scoreFromResult);

        batchScore.set(scoreSoc, scoreDataProfile, SetOptions.merge());

        batchScore.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if (g_testList.get(g_selected_test_index).getAttempt() <= 3) {
                        //g_testList.get(g_selected_test_index).setAttempt(g_attempt);

                        completeListener.onSuccess();
                        //} else {
                        //    completeListener.onSuccess();
                        //}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

        DocumentReference leaderboardDoc = g_fireStore.collection("LEADERBOARD").document("TESTS_LIST");

        if (scoreFromResult > 0) {
            DocumentReference scoreDoc = leaderboardDoc.collection(g_testList.get(g_selected_test_index).getTestId()).document(FirebaseAuth.getInstance().getUid());

            Map<String, Object> scoreDataRank = new ArrayMap<>();
            scoreDataRank.put("SCORE", scoreFromResult);

            batchLeaderboard.set(scoreDoc, scoreDataRank, SetOptions.merge());
        }

        batchLeaderboard.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //if (g_testList.get(g_selected_test_index).getAttempt() <= 3) {
                            //g_testList.get(g_selected_test_index).setAttempt(g_attempt);

                            completeListener.onSuccess();
                        //} else {
                        //    completeListener.onSuccess();
                        //}
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });


    }
}
