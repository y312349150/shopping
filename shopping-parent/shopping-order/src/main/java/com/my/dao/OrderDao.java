package com.my.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface OrderDao {
	@Update("update order_info set ispay=#{isPay}, payId=#{payId} WHERE orderNumber=#{orderNumber};")
	public int updateOrder(@Param("isPay") long isPay,@Param("payId") String payId,@Param("orderNumber") String orderNumber);
}
