package ro.unibuc.hello.service;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.hello.data.DishesEntity;
import ro.unibuc.hello.data.DishesRepository;
import ro.unibuc.hello.dto.DishesDTO;
import ro.unibuc.hello.service.DishesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishesServiceIntegrationTest {

    private DishesService dishesService;

    @Mock
    private DishesRepository dishesRepository;

    @BeforeEach
    void setUp() {
        dishesService = new DishesService();
        dishesService.setDishesRepository(dishesRepository);
    }

    @Test
    void testGetDishes() {

        List<DishesEntity> dishesEntities = new ArrayList<>();
        dishesEntities.add(new DishesEntity("dish1", 2, 5.99f));
        dishesEntities.add(new DishesEntity("dish2", 1, 8.99f));
        when(dishesRepository.findAll()).thenReturn(dishesEntities);

        List<DishesDTO> dishesDTOs = dishesService.getDishes();

        assertEquals(2, dishesDTOs.size());
        assertEquals("dish1", dishesDTOs.get(0).getName());
        assertEquals(2, dishesDTOs.get(0).getQuantity());
        assertEquals(5.99f, dishesDTOs.get(0).getPrice());
        assertEquals("dish2", dishesDTOs.get(1).getName());
        assertEquals(1, dishesDTOs.get(1).getQuantity());
        assertEquals(8.99f, dishesDTOs.get(1).getPrice());
    }

    @Test
    void testGetDish() {

        DishesEntity dishEntity = new DishesEntity("dish1", 2, 5.99f);
        String dishId = dishEntity.getId().toString();
        when(dishesRepository.findById(dishId)).thenReturn(Optional.of(dishEntity));

        DishesDTO dishDTO = dishesService.getDish(dishId);

        assertEquals("dish1", dishDTO.getName());
        assertEquals(2, dishDTO.getQuantity());
        assertEquals(5.99f, dishDTO.getPrice());
    }

    @Test
    void testInsertDish() {

    }
        DishesEntity dishEntity = new DishesEntity("dish1", 2, 5.99f);
        when(dishesRepository.save(any(DishesEntity.class))).thenReturn(dishEntity);

        DishesDTO dishDTO = dishesService.insertDish("dish1", 2, 5.99f);

        assertEquals("dish1", dishDTO.getName());
        assertEquals(2, dishDTO.getQuantity());
        assertEquals(5.99f, dishDTO.getPrice());
    }

    @Test
    void testUpdateDish() {

        DishesEntity dishEntity = new DishesEntity("dish1", 2, 5.99f);
        String dishId = dishEntity.getId().toString();
        when(dishesRepository.findById(dishId)).thenReturn(Optional.of(dishId))

        DishesDTO updatedDishDTO = dishesService.updateDish(dishId, "newDishName", 5, 9.99f);

        assertEquals("newDishName", updatedDishDTO.getName());
        assertEquals(5, updatedDishDTO.getQuantity());
        assertEquals(9.99f, updatedDishDTO.getPrice());
    }

    @Test
    void testDeleteDish() {
        dishesRepository.deleteAll();

        DishesEntity dishEntity = new DishesEntity("dish1", 2, 5.99f);
        String dishId = dishEntity.getId().toString();
        String result = dishesService.deleteDish(dishId);

        assertEquals("Dish with id " + dishId + " was deleted successfully!", result);
    }
