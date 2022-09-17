<script>
    /** @type {import('./$types').Page} */
    export let data;
</script>

<svelte:head>
    <title>Song - {data.name}</title>
</svelte:head>

<table class="table text-light">
    <tr>
        <th>Name</th>
        <th>Value</th>
    </tr>
    <tr>
        <td>Id</td>
        <td>{data.id}</td>
    </tr>
    <tr>
        <td>Name</td>
        <td>{data.name}</td>
    </tr>
    <tr>
        <td>Tags</td>
        <td>
            {#each data.tags as tag}
                <span class="badge bg-primary me-1">{tag}</span>
            {/each}
        </td>
    </tr>
    <tr>
        <td>Playback</td>
        <td>
            <audio controls>
                <source src="http://localhost:8080/song/{data.id}/file" >
            </audio>
        </td>
    </tr>
</table>
