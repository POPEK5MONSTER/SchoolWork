using Kolos2.Services;
using Microsoft.AspNetCore.Mvc;

namespace Kolos2.Controller;

[ApiController]
public class Game_Controller : ControllerBase
{
    public IDbService Service;

    public Game_Controller(IDbService service)
    {
        Service = service;
    }

    [HttpGet]
    [Route("api/characters/{characterId:int}")]
    public async Task<IActionResult> GetCharacters(int characterId)
    {
        if (!await Service.DoesCharExist(characterId))
        {
            return NotFound($"Postac o tyn id: {characterId} nie istnieje");
        }
        
        var result = await Service.GetChar(characterId);

        return Ok(result);
    }
    
    [HttpPost]
    [Route("api/characters/{characterId:int}/backpacks")]
    public async Task<IActionResult> AddItems(int characterId, List<int> newIdItems)
    {
        foreach (var id in newIdItems)
        {
            if (!await Service.DoesItemExist(id))
            {
                return NotFound($"Przedmiot o danym id: {id} nie istnieje");
            }
        }

        if (!await Service.DoesCharHaveEnoughtWeight(characterId, newIdItems))
        {
            return NotFound($"Postac o podanym id: {characterId} nie ma wystarczająco miejsca w plecaku");
        }

        await Service.AddItemsToBackpack(characterId, newIdItems);

        var result = await Service.GetCharactersItems(characterId);

        return Ok(result);
    }
}