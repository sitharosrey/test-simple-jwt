//package com.example.pvhjwt.repo;
//
//import com.example.pvhjwt.model.AppUser;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//
//@Mapper
//public interface UserRepo {
//
//    @Select("""
//            SELECT *
//            FROM user_tb
//            WHERE email = #{email};
//            """)
//    @Results(id = "userMapper", value = {
//            @Result(property = "phoneNumber", column = "phone"),
//            @Result(property = "id", column = "id"),
//            @Result(property = "roles", column = "id", many = @Many(select = "getRolesByUserId")),
//    })
//    AppUser getUserByEmail(String email);
//
//    @Select("""
//            SELECT rt.name
//            FROM user_role_tb ur INNER JOIN role_tb rt ON rt.id = ur.role_id
//            WHERE user_id = #{userId}
//            """)
//    List<String> getRolesByUserId(Integer userId);
//}
