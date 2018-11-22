package com.my.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.my.entity.PaymentInfo;

@Mapper
public interface PaymentInfoDao {

	@Select("select * from payment_info where  id=#{id}")
	public PaymentInfo getPaymentInfo(@Param("id") Long id);

	@Insert("insert into payment_info ( id,userid,typeid,orderid,platformorderid,price,source,state,created,updated) value(null,#{userId},#{typeId},#{orderId},#{platformorderId},#{price},#{source},#{state},#{created},#{updated})")
	@Options(useGeneratedKeys = true, keyProperty = "id") // 添加该行，将自动增长的ID返回给对象
	public Integer savePaymentType(PaymentInfo paymentInfo);

	@Select("select * from payment_info where  orderId=#{orderId}")
	public PaymentInfo getByOrderIdPayInfo(@Param("orderId") String orderId);

	@Update("update payment_info set state =#{state},payMessage=#{payMessage},platformorderId=#{platformorderId},updated=#{updated} where orderId=#{orderId} ")
	public Integer updatePayInfo(PaymentInfo paymentInfo);
}