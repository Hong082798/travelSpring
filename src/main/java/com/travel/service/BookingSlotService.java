package com.travel.service;

import com.travel.dto.BookingSlotCreateDTO;
import com.travel.vo.BookingSlotVO;

import java.util.List;

public interface BookingSlotService {

  Long createSlot( BookingSlotCreateDTO dto );

  List < BookingSlotVO > listAvailableSlots( String targetType, Long targetId );

  void disableSlot( Long slotId );

}
